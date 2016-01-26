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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joseph.app.adapter.moduleListViewAdapter;
import com.example.joseph.app.helper.ActiveUser;
import com.example.joseph.app.helper.ApiIntra;
import com.example.joseph.app.helper.MarkInfo;
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
    private String currentSemester = "1";
    private String currentCodeModule = "";
    private ArrayList<String> ModulesArrays = null;
    private ArrayList<MarkInfo> MarksArrays = null;

    private View view;

    private OnFragmentInteractionListener mListener;

    public GradeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GradeFragment.
     */
    public static GradeFragment newInstance(String param1, String param2) {
        GradeFragment fragment = new GradeFragment();
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
        view = inflater.inflate(R.layout.fragment_grade, container, false);
        return view;
    }


    private void getAllMarks() {
        final android.os.Handler handler = new android.os.Handler();
        new Thread(new Runnable() {
            public void run() {
                try {
                    SharedPreferences prefs = getActivity().getPreferences(getActivity().MODE_PRIVATE);
                    final ActiveUser user = ((FrontPageActivity)getActivity()).getUser();

                    ApiIntra.getMarks();
                    String MarksJson = prefs.getString("marks", null);
                    final JsonArray MarksArray = JsonGrabber.getArrayFromPath(MarksJson, "notes");

                    ApiIntra.getModules();
                    String ModuleJson = prefs.getString("modules", null);
                    final JsonArray ModulesArray = JsonGrabber.getArrayFromPath(ModuleJson, "modules");

                    if (MarksArray == null || ModulesArray == null)
                        return;

                    final ArrayList<String> semester = new ArrayList<String>();

                    for (int i = 0; i <= user.getSemester(); i++) {
                        semester.add(i, "Semester " + Integer.toString(i));
                    }

//                    ModulesArrays = sortModuleBySemester(ModulesArray, currentSemester);
//                    MarksArrays = sortMarksBySemesterAndModule(MarksArray, currentSemester, currentModule);

                    final ListView lv = (ListView) getActivity().findViewById(R.id.SemestreListView);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                            currentSemester = Integer.toString(myItemInt);
                            ModulesArrays = sortModuleBySemester(ModulesArray, currentSemester);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ListView ModuleListView = (ListView) getActivity().findViewById(R.id.ModuleListView);
                                    semesterListViewAdapter ModuleCustomAdapter = new semesterListViewAdapter(getActivity().getApplicationContext(), ModulesArrays);
                                    ModuleListView.setAdapter(ModuleCustomAdapter);
                                }
                            });
                        }});

                    final ListView lv2 = (ListView) getActivity().findViewById(R.id.ModuleListView);
                    lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                            currentCodeModule = Integer.toString(myItemInt);
                            if (MarksArray != null)
                                MarksArrays = sortMarksBySemesterAndModule(MarksArray, currentCodeModule);
                            if (MarksArrays != null) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ListView MarksListView = (ListView) getActivity().findViewById(R.id.MarksListView);
                                        markListViewAdapter MarkscustomAdapter = new markListViewAdapter(getActivity().getApplicationContext(), MarksArrays);
                                        MarksListView.setAdapter(MarkscustomAdapter);
                                    }
                                });
                            }
                        }});

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            // TODO afficher par rapport au semestre et a la matière.
                            ListView SemesterListView = (ListView)getActivity().findViewById(R.id.SemestreListView);
                            semesterListViewAdapter SemesterCustomAdapter = new semesterListViewAdapter(getActivity().getApplicationContext(), semester);
                            SemesterListView.setAdapter(SemesterCustomAdapter);

                            ListView ModuleListView = (ListView) getActivity().findViewById(R.id.ModuleListView);
                            semesterListViewAdapter ModuleCustomAdapter = new semesterListViewAdapter(getActivity().getApplicationContext(), ModulesArrays);
                            ModuleListView.setAdapter(ModuleCustomAdapter);

                            ListView MarksListView = (ListView) getActivity().findViewById(R.id.MarksListView);
                            markListViewAdapter MarkscustomAdapter = new markListViewAdapter(getActivity().getApplicationContext(), MarksArrays);
                            MarksListView.setAdapter(MarkscustomAdapter);
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        }).start();
    }

    private ArrayList<String> sortModuleBySemester(JsonArray array, String semester) {
        final ArrayList<String> sorted = new ArrayList<String>();
        for (int i = 0; i < array.size(); i++){
            String tmp;
            JsonObject obj = (JsonObject)array.get(i);
            if (obj != null) {
                tmp = obj.get("semester").getAsString();
                if (tmp == semester)
                    sorted.add(obj.get("title").getAsString());
            }
        }
        return sorted;
    }

    private ArrayList<MarkInfo> sortMarksBySemesterAndModule(JsonArray array, String Module) {
        final ArrayList<MarkInfo> sorted = new ArrayList<MarkInfo>();
        if (array == null)
            return null;
        for (int i = 0; i < array.size(); i++){
            String tmp;
            JsonObject obj = (JsonObject)array.get(i);
            if (obj != null) {
                if (obj.get("titlemodule") != null)
                {
                   String module = obj.get("titlemodule").getAsString();
                   if (ModulesArrays != null && ModulesArrays.get(Integer.parseInt(Module)) != null && module.equalsIgnoreCase(ModulesArrays.get(Integer.parseInt(Module)))) {
                        sorted.add(new MarkInfo(obj.get("title").getAsString(), obj.get("correcteur").getAsString(), obj.get("comment").getAsString(), obj.get("final_note").getAsString()));
                    }
                }
            }

        }
        return sorted;
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
    void onFragmentInteraction(Uri uri);
}
}
