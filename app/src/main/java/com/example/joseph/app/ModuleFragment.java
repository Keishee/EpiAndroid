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
import android.widget.ListView;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ModuleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModuleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModuleFragment newInstance(String param1, String param2) {
        ModuleFragment fragment = new ModuleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_module, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
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
                            moduleListViewAdapter customAdapter = new moduleListViewAdapter(getActivity().getApplicationContext(), array);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
