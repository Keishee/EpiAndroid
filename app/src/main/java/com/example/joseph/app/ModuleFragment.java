package com.example.joseph.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.joseph.app.adapter.messageListViewAdapter;
import com.example.joseph.app.adapter.moduleListViewAdapter;
import com.example.joseph.app.helper.ApiIntra;
import com.example.joseph.app.json.JsonGrabber;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ModuleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ModuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModuleFragment extends Fragment {
    private final String TAG = "ModuleFragment";
    private OnFragmentInteractionListener mListener;

    private View rootView;

    private Boolean registered = true;

    public ModuleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ModuleFragment.
     */
    public static ModuleFragment newInstance(String param1, String param2) {
        ModuleFragment fragment = new ModuleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_module, container, false);
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Switch s = (Switch) rootView.findViewById(R.id.moduleSwitch);
        s.setChecked(true);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !registered) {
                    registered = true;
                    loadModule();
                } else if (!isChecked && registered) {
                    registered = false;
                    loadModule();
                }
            }
        });
        loadModule();
    }

    public void loadModule() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                try {
                    String login = ((FrontPageActivity) getActivity()).getLogin();
                    final String response = ApiIntra.getUser(login);

                    Calendar c = Calendar.getInstance();
                    String year = "" + c.get(Calendar.YEAR);
                    String location = JsonGrabber.getVariableAndCast(response, "location");
                    String course = JsonGrabber.getVariableAndCast(response, "course_code");
                    String modules = ApiIntra.getAllModules(year, location, course);

//                    SharedPreferences prefs = getActivity().getPreferences(getActivity().MODE_PRIVATE);
//                    String modules = prefs.getString("allModules", "");
                    if (modules.isEmpty())
                        return;

                    Log.i(TAG, modules);

                    JsonArray arr = JsonGrabber.getArrayFromPath(modules, "items");

                    if (registered) {
                        JsonArray tmp = new JsonArray();
                        for (JsonElement obj : arr) {
                            JsonObject obj2 = obj.getAsJsonObject();
                            if (obj2.get("status").getAsString().equals("ongoing")) {
                                tmp.add(obj);
                            }
                        }
                        arr = tmp;
                    }

                    final JsonArray array = arr;

                    Log.i(TAG, array.toString());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (getActivity() == null)
                                return ;
                            ListView yourListView = (ListView) getActivity().findViewById(R.id.moduleListView);
                            if (yourListView == null)
                                return;
                            moduleListViewAdapter customAdapter = new moduleListViewAdapter(getActivity().getApplicationContext(), array);
                            yourListView.setAdapter(customAdapter);

                            yourListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    View v = rootView.findViewById(R.id.moduleLayout);
                                    v.setVisibility(View.INVISIBLE);
                                    v = rootView.findViewById(R.id.projectLayout);
                                    v.setVisibility(View.VISIBLE);
                                    loadProjects(array, position);
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }).start();
    }

    public void loadProjects(JsonArray array, int postion) {
        final Handler handler = new Handler();
        final JsonObject jo = (JsonObject) array.get(postion);
        new Thread(new Runnable() {
            public void run() {
                try {
                    String code = jo.get("code").getAsString();
                    String codeinstance = jo.get("codeinstance").getAsString();
                    Calendar c = Calendar.getInstance();
                    String year = "" + c.get(Calendar.YEAR);
                    String json = ApiIntra.getModule("2015", code, codeinstance);
                    final String descrition = JsonGrabber.getVariableAndCast(json, "description");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView tv = (TextView) rootView.findViewById(R.id.descriptionTextView);
                            tv.setText(descrition);
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }).start();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
