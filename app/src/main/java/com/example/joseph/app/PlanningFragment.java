package com.example.joseph.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
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
import com.example.joseph.app.adapter.planningListViewAdapter;
import com.example.joseph.app.helper.ActiveUser;
import com.example.joseph.app.helper.ApiIntra;
import com.example.joseph.app.helper.ApiManager;
import com.example.joseph.app.helper.PlanningInfo;
import com.example.joseph.app.json.JsonGrabber;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlanningFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlanningFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlanningFragment extends Fragment {
    private static final String TAG = "PlanningFragment";
    private ArrayList<PlanningInfo> planningInfos;
    private OnFragmentInteractionListener mListener;

    public PlanningFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlanningFragment.
     */
    public static PlanningFragment newInstance() {
        PlanningFragment fragment = new PlanningFragment();
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
        return inflater.inflate(R.layout.fragment_planning, container, false);
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

    private int getUserSemester() {
        final ActiveUser user = ((FrontPageActivity)getActivity()).getUser();
        String response = ApiIntra.getUser(user.getLogin());
        return JsonGrabber.getVariableAndCast(response, "semester");
    }

    private void getWeeklySemesterCoursesAndShow() {
        final ActiveUser user = ((FrontPageActivity)getActivity()).getUser();
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int semester = user.getSemester();
                if (semester == -1) {
                    semester = getUserSemester();
                    user.setSemester(semester);
                }
                Date cDate = new Date();
                String date = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, +14);
                String datePlusWeek =  new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

                ApiIntra.getPlanning(date, datePlusWeek);
                SharedPreferences prefs = getActivity().getPreferences(getActivity().MODE_PRIVATE);
                String response = prefs.getString("planning", null);
                JsonParser parser = new JsonParser();
                JsonArray array = (JsonArray) parser.parse(response);

                planningInfos = new ArrayList<>();
                for (JsonElement elem : array) {
                    JsonObject obj = elem.getAsJsonObject();
                    if (obj.get("semester").getAsInt() == semester) {
                        String title = obj.get("acti_title").getAsString();
                        int csemester = obj.get("semester").getAsInt();
                        String room = ((JsonObject) obj.get("room")).get("code").getAsString();
                        String start = obj.get("start").getAsString();
                        String end = obj.get("end").getAsString();
                        String module = obj.get("titlemodule").getAsString();

                        String eventRegistered = obj.get("event_registered").getAsString();
                        planningInfos.add(new PlanningInfo(title, module, csemester, room, start, end, eventRegistered.equals("registered")));
                    }
                }
                Collections.sort(planningInfos, new Comparator<PlanningInfo>() {
                    public int compare(PlanningInfo p1, PlanningInfo p2) {
                        return (int)(p1.getStartDate().getTime() - p2.getStartDate().getTime());
                    }
                });
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
        try {
            getWeeklySemesterCoursesAndShow();
            Switch rSwitch = (Switch)getView().findViewById(R.id.registerSwitch);
            rSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (planningInfos != null) {
                        ListView yourListView = (ListView) getActivity().findViewById(R.id.planningListView);
                        planningListViewAdapter adapter = (planningListViewAdapter) yourListView.getAdapter();
                        if (isChecked) {
                            ArrayList<PlanningInfo> onlyRegistered = new ArrayList<>();
                            for (PlanningInfo pi : planningInfos) {
                                if (pi.isRegistered())
                                    onlyRegistered.add(pi);
                            }
                            adapter.setPlanningInfos(onlyRegistered);
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter.setPlanningInfos(planningInfos);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
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
        void onFragmentInteraction(Uri uri);
    }
}
