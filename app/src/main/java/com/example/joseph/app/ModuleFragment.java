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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.example.joseph.app.adapter.messageListViewAdapter;
import com.example.joseph.app.adapter.moduleListViewAdapter;
import com.example.joseph.app.helper.ApiIntra;
import com.example.joseph.app.json.JsonGrabber;
import com.google.gson.JsonArray;

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

                    final JsonArray array = JsonGrabber.getArrayFromPath(modules, "items");

                    Log.i(TAG, array.toString());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ListView yourListView = (ListView) getActivity().findViewById(R.id.moduleListView);
                            moduleListViewAdapter customAdapter = new moduleListViewAdapter(getActivity().getApplicationContext(), array, registered);
                            yourListView.setAdapter(customAdapter);
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
