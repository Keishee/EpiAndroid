package com.example.joseph.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.joseph.app.helper.ApiIntra;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.MalformedURLException;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    private void getUserImageAndShow() {
        final ImageView image = (ImageView)view.findViewById(R.id.photo_home);
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                try {
                    ApiIntra.getPhoto(((FrontPageActivity) getActivity()).getLogin());
                    SharedPreferences prefs = getActivity().getPreferences(getActivity().MODE_PRIVATE);
                    String url = prefs.getString("photo", null);
                    JsonParser jp = new JsonParser();
                    JsonObject jo = (JsonObject) jp.parse(url);
                    JsonElement je = jo.get("url");
                    url = je.getAsString();
                    InputStream is = new URL(url).openStream();
                    final Drawable d = Drawable.createFromStream(is, "picture");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            image.setImageDrawable(d);
                        }
                    });
                } catch (Exception e) {
                    Log.e("ImageView", e.getMessage());
                }
            }
        }).start();
    }

    private void getUserLogTimeAndShow() {
        final Handler handler = new Handler();
        final TextView log = (TextView)view.findViewById(R.id.logTextView);
        new Thread(new Runnable() {
            public void run() {
                try {
                    String login = ((FrontPageActivity)getActivity()).getLogin();
                    String response = ApiIntra.getUser(login);
                    JsonParser jp = new JsonParser();
                    JsonObject jresp = (JsonObject)jp.parse(response);
                    JsonObject jo = (JsonObject)jresp.get("nsstat");
                    final String hours = jo == null ? "0" : jo.get("active").getAsString();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            log.setText("Active time: " + hours + " hour(s)");
                        }
                    });
                } catch (Exception e) {
                    Log.e("ImageView", e.getMessage());
                }
            }
        }).start();
    }

    private void getLastMessagesAndShow() {
        final Handler handler = new Handler();
//        final ListView messages = (ListView)view.findViewById(R.id.logTextView);
        new Thread(new Runnable() {
            public void run() {
                ApiIntra.getMessages();
                SharedPreferences prefs = getActivity().getPreferences(getActivity().MODE_PRIVATE);
                String response = prefs.getString("messages", null);
                JsonParser jp = new JsonParser();
                final JsonArray array = (JsonArray)jp.parse(response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        for (JsonElement elem : array) {
//                            JsonObject obj = (JsonObject)elem;
//                            String title = obj.get("title").getAsString();
//                            String content = obj.get("content").getAsString();
//                        }

                    }
                });
            }
        }).start();
    }

    @Override
    public void onStart() {
        super.onStart();
        getUserImageAndShow();
        getUserLogTimeAndShow();
        getLastMessagesAndShow();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
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
