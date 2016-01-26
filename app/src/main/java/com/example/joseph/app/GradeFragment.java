package com.example.joseph.app;

import com.example.joseph.app.adapter.markListViewAdapter;
import com.example.joseph.app.adapter.semesterListViewAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joseph.app.adapter.moduleListViewAdapter;
import com.example.joseph.app.helper.ActiveUser;
import com.example.joseph.app.helper.ApiIntra;
import com.example.joseph.app.json.JsonGrabber;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.google.gson.JsonArray;
//import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Handler;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GradeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GradeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GradeFragment extends Fragment {
    private final String TAG = "GradeFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;

    private OnFragmentInteractionListener mListener;

    public GradeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GradeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GradeFragment newInstance(String param1, String param2) {
        GradeFragment fragment = new GradeFragment();
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
        view = inflater.inflate(R.layout.fragment_grade, container, false);
        return view;
//        return inflater.inflate(R.layout.fragment_grade, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void getAllMarks() {
        final android.os.Handler handler = new android.os.Handler();
        new Thread(new Runnable() {
            public void run() {
                try {
                    SharedPreferences prefs = getActivity().getPreferences(getActivity().MODE_PRIVATE);

                    ApiIntra.getMarks();
                    String MarksJson = prefs.getString("marks", null);
                    final JsonArray MarksArray = JsonGrabber.getArrayFromPath(MarksJson, "notes");

                    ApiIntra.getModules();
                    String ModuleJson = prefs.getString("modules", null);
                    final JsonArray ModulesArray = JsonGrabber.getArrayFromPath(ModuleJson, "modules");

                    if (MarksArray == null || ModulesArray == null)
                        return;

                    final ActiveUser user = ((FrontPageActivity)getActivity()).getUser();

                    final ArrayList<String> semester = new ArrayList<String>();;

                    for (int i = 0; i < user.getSemester(); i++) {
                        semester.add(i, "Semester " + Integer.toString(i + 1));
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            // TODO afficher par rapport au semestre et a la matière.
                            ListView SemesterListView = (ListView)getActivity().findViewById(R.id.SemestreListView);
                            semesterListViewAdapter SemesterCustomAdapter = new semesterListViewAdapter(getActivity().getApplicationContext(), semester);
                            SemesterListView.setAdapter(SemesterCustomAdapter);

                            ListView ModuleListView = (ListView) getActivity().findViewById(R.id.ModuleListView);
                            moduleListViewAdapter ModuleCustomAdapter = new moduleListViewAdapter(getActivity().getApplicationContext(), ModulesArray);
                            ModuleListView.setAdapter(ModuleCustomAdapter);

                            ListView MarksListView = (ListView) getActivity().findViewById(R.id.MarksListView);
                            markListViewAdapter MarkscustomAdapter = new markListViewAdapter(getActivity().getApplicationContext(), MarksArray);
                            MarksListView.setAdapter(MarkscustomAdapter);
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }).start();
    }

    private JsonArray sortBySemester(JsonArray array, String semester) {
        JsonArray sorted;
        return null;
    }


    @Override
    public void onStart() {
        super.onStart();
        getAllMarks();
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
