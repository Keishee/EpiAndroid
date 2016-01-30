package com.example.joseph.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joseph.app.adapter.messageListViewAdapter;
import com.example.joseph.app.helper.ActiveUser;
import com.example.joseph.app.helper.ApiIntra;
import com.example.joseph.app.json.JsonGrabber;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private View view;
    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getUserImageAndShow() {
        final ActiveUser user = ((FrontPageActivity)getActivity()).getUser();
        if (user.getUserImage() != null) {
            ImageView image = (ImageView) view.findViewById(R.id.photo_home);
            image.setImageDrawable(user.getUserImage());
            return;
        }
        try {
            final Handler handler = new Handler();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        ApiIntra.getPhoto(((FrontPageActivity) getActivity()).getLogin());
                        SharedPreferences prefs = getActivity().getPreferences(getActivity().MODE_PRIVATE);
                        String response = prefs.getString("photo", null);
                        final String url2 = JsonGrabber.getVariableAndCast(response, "url");
                        InputStream is = new URL(url2).openStream();
                        final Drawable d = Drawable.createFromStream(is, "picture");
                        user.setUserImage(d);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ImageView image = (ImageView) view.findViewById(R.id.photo_home);
                                image.setImageDrawable(d);
                            }
                        });
                    } catch (Exception e) {
                        Log.e("getUserImageAndShow", "Error: " + e.getMessage());
                    }
                }
            }).start();

        } catch (Exception e) {
            Log.e("getUserImageAndShow", "Error: " + e.getMessage());
        }

    }

    private void getUserInfosAndShow() {
        final ActiveUser user = ((FrontPageActivity)getActivity()).getUser();
        if (user.getFullName() != null && user.getGPA() != null && user.getLogTime() != null) {
            TextView log = (TextView) view.findViewById(R.id.logTextView);
            log.setText("Log: " + user.getLogTime() + " hour(s)");
            ((TextView) view.findViewById(R.id.userName)).setText(user.getFullName());
            ((TextView) view.findViewById(R.id.userGPA)).setText("GPA: " + user.getGPA());
            return;
        }

        final Handler handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                try {
                    String login = ((FrontPageActivity) getActivity()).getLogin();
                    final String response = ApiIntra.getUser(login);

                    String hour = JsonGrabber.getVariableAndCast(response, "nsstat", "active");
                    final String hours = hour == null ? "0" : hour;
                    String name = JsonGrabber.getVariableAndCast(response, "title");
                    final String goodName = name == null ? "Leeroy Jenkins" : name;
                    String gpa = JsonGrabber.getVariableAndCast(response, "gpa", "gpa");
                    final String goodGPA = gpa == null ? "0" : gpa;
                    user.setLogTime(hours);
                    user.setFullName(goodName);
                    user.setGPA(goodGPA);
                    user.setSemester(Integer.parseInt((String)JsonGrabber.getVariableAndCast(response, "semester")));

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView log = (TextView) view.findViewById(R.id.logTextView);
                            log.setText("Log: " + hours + " hour(s)");
                            ((TextView) view.findViewById(R.id.userName)).setText(goodName);
                            ((TextView) view.findViewById(R.id.userGPA)).setText("GPA: " + goodGPA);
                        }
                    });
                } catch (Exception e) {
                    Log.e("getUserImageAndShow","Error:" + e.getMessage());
                }
            }
        }).start();

    }

    private void getLastMessagesAndShow() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                try {
                    ApiIntra.getMessages();
                    SharedPreferences prefs = getActivity().getPreferences(getActivity().MODE_PRIVATE);
                    String response = prefs.getString("messages", null);
                    JsonParser jp = new JsonParser();
                    final JsonArray array = (JsonArray) jp.parse(response);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ListView yourListView = (ListView) getActivity().findViewById(R.id.messageListView);
                            if (yourListView == null)
                                return ;
                            messageListViewAdapter customAdapter = new messageListViewAdapter(getActivity().getApplicationContext(), array);
                            yourListView.setAdapter(customAdapter);
                        }
                    });
                } catch (Exception e) {
                    Log.e("getUserImageAndShow", "Error:" + e.getMessage());
                }
            }
        }).start();
    }

    @Override
    public void onStart() {
        super.onStart();
        getUserImageAndShow();
        getUserInfosAndShow();
        getLastMessagesAndShow();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
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
