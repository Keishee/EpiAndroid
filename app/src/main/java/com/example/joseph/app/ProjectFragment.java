package com.example.joseph.app;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.example.joseph.app.adapter.ProjectMainListViewAdapter;
import com.example.joseph.app.adapter.moduleListViewAdapter;
import com.example.joseph.app.adapter.projectListViewAdapter;
import com.example.joseph.app.helper.ApiIntra;
import com.example.joseph.app.json.JsonGrabber;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectFragment extends Fragment {
    private final String TAG = "ProjectFragment";

    public ProjectFragment() {
        // Required empty public constructor
    }

    public static ProjectFragment newInstance(String param1, String param2) {
        ProjectFragment fragment = new ProjectFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return rootView = inflater.inflate(R.layout.fragment_project, container, false);
    }

    private Boolean registered = false;

    @Override
    public void onStart() {
        super.onStart();
        Switch s = (Switch) rootView.findViewById(R.id.projectSwitch);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !registered) {
                    registered = true;
                    loadProject();
                } else if (!isChecked && registered) {
                    registered = false;
                    loadProject();
                }
            }
        });
        Button b = (Button) rootView.findViewById(R.id.projectRefreshButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshProject();
            }
        });
        loadProject();
    }

    public void loadProject() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                try {
                    SharedPreferences prefs = getActivity().getPreferences(getActivity().MODE_PRIVATE);
                    String projects = prefs.getString("projects", "");

                    if (projects.isEmpty()) {
                        projects = ApiIntra.getProjects();
                    }

                    JsonArray array = JsonGrabber.getArrayFromPath(projects);

                    JsonArray tmp = new JsonArray();
                    for (JsonElement e : array) {
                        JsonObject jo = e.getAsJsonObject();

                        JsonElement test = jo.get("project");
                        if (!test.isJsonNull()) {
                            String r = jo.get("registered").getAsString();
                            if ((registered && r.equals("1")) || !registered)
                                tmp.add(e);
                        }
                    }

                    final JsonArray arr = tmp;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ListView yourListView = (ListView) rootView.findViewById(R.id.projectMainListView);
                            if (yourListView == null)
                                return;
                            ProjectMainListViewAdapter customAdapter = new ProjectMainListViewAdapter(getActivity().getApplicationContext(), arr);
                            yourListView.setAdapter(customAdapter);
                        }
                    });

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }).start();
    }

    public void refreshProject() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                try {

                    String projects = ApiIntra.getProjects();


                    JsonArray array = JsonGrabber.getArrayFromPath(projects);

                    JsonArray tmp = new JsonArray();
                    for (JsonElement e : array) {
                        JsonObject jo = e.getAsJsonObject();

                        JsonElement test = jo.get("project");
                        if (!test.isJsonNull()) {
                            String r = jo.get("registered").getAsString();
                            if ((registered && r.equals("1")) || !registered)
                                tmp.add(e);
                        }
                    }

                    final JsonArray arr = tmp;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ListView yourListView = (ListView) rootView.findViewById(R.id.projectMainListView);
                            if (yourListView == null)
                                return;
                            ProjectMainListViewAdapter customAdapter = new ProjectMainListViewAdapter(getActivity().getApplicationContext(), arr);
                            yourListView.setAdapter(customAdapter);
                        }
                    });

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }).start();
    }

}
