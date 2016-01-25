package com.example.joseph.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.joseph.app.adapter.messageListViewAdapter;
import com.example.joseph.app.adapter.planningListViewAdapter;
import com.example.joseph.app.helper.ApiIntra;
import com.example.joseph.app.helper.ApiManager;
import com.example.joseph.app.helper.PlanningInfo;
import com.example.joseph.app.json.JsonGrabber;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlanningFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlanningFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlanningFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PlanningFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlanningFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlanningFragment newInstance(String param1, String param2) {
        PlanningFragment fragment = new PlanningFragment();
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
        return inflater.inflate(R.layout.fragment_planning, container, false);
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

    private void getWeeklySemesterCoursesAndShow() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // TODO: set 1 week dates
                String date = "2016-01-25";//String.valueOf(year) + '-' + String.valueOf(month) + '-' + String.valueOf(day);
                String endDate = "2016-01-29";//String.valueOf(year) + '-' + String.valueOf(month) + '-' + String.valueOf(day);
                ApiIntra.getPlanning(date, endDate);
                SharedPreferences prefs = getActivity().getPreferences(getActivity().MODE_PRIVATE);
                String response = prefs.getString("planning", null);
                JsonParser parser = new JsonParser();
                JsonArray array = (JsonArray) parser.parse(response);
                final ArrayList<PlanningInfo> planningInfos = new ArrayList<>();

                for (JsonElement elem : array) {
                    JsonObject obj = elem.getAsJsonObject();
                    if (obj.get("semester").getAsInt() == 5) { // TODO: recup le semestre courant
                        String title = obj.get("acti_title").getAsString();
                        int semester = obj.get("semester").getAsInt();
                        String room = ((JsonObject) obj.get("room")).get("code").getAsString();
                        String start = obj.get("start").getAsString();
                        String end = obj.get("end").getAsString();
                        String module = obj.get("titlemodule").getAsString();
                        planningInfos.add(new PlanningInfo(title, module, semester, room, start, end));
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ListView yourListView = (ListView) getActivity().findViewById(R.id.planningListView);
                        planningListViewAdapter customAdapter = new planningListViewAdapter(getActivity().getApplicationContext(), planningInfos);
                        yourListView.setAdapter(customAdapter);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onStart() {
        super.onStart();
        getWeeklySemesterCoursesAndShow();
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
