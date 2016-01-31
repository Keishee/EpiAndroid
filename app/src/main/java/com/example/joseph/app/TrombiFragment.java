package com.example.joseph.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joseph.app.helper.ApiIntra;
import com.example.joseph.app.json.JsonGrabber;

import java.io.InputStream;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrombiFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrombiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrombiFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private EditText editText;

    public TrombiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TrombiFragment.
     */
    public static TrombiFragment newInstance(String param1, String param2) {
        TrombiFragment fragment = new TrombiFragment();
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
        return inflater.inflate(R.layout.fragment_trombi, container, false);
    }

    private void initializeComponents() {

        final Button button = (Button) getActivity().findViewById(R.id.buttonSearch);
        editText = (EditText) getActivity().findViewById(R.id.loginTrombiText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                String login = editText.getEditableText().toString();
                if (login.length() == 0) {
                    editText.setError("Wrong login");
                    return;
                }
                getUsersAndShow(login);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeComponents();

    }

    private void getUsersAndShow(final String login) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String response = ApiIntra.getUser(login);
                String hour = JsonGrabber.getVariable(response, "nsstat", "active");
                final String hours = hour == null ? "0" : hour;
                String name = JsonGrabber.getVariable(response, "title");
                final String goodName = name == null ? "Leeroy Jenkins" : name;
                String gpa = JsonGrabber.getVariable(response, "gpa", "gpa");
                final String goodGPA = gpa == null ? "0" : gpa;
                String promo = JsonGrabber.getVariable(response, "promo");
                final String goodPromo = promo == null ? "2018" : promo;
                String loc = JsonGrabber.getVariable(response, "location");
                final String goodloc = loc == null ? "FR/PAR" : loc;
                final String phone = JsonGrabber.getVariable(response, "userinfo", "telephone", "value");
                final String goodphone = phone == null ? "N/A" : phone;

                Drawable d = null;
                try {
                    ApiIntra.getPhoto(login);
                    SharedPreferences prefs = getActivity().getPreferences(getActivity().MODE_PRIVATE);
                    String photoResponse = prefs.getString("photo", null);
                    final String url2 = JsonGrabber.getVariable(photoResponse, "url");
                    InputStream is = new URL(url2).openStream();
                    d = Drawable.createFromStream(is, "picture");
                } catch (Exception e) {
                    Log.e("getUserImageAndShow", "Error: " + e.getMessage());
                }
                final Drawable goodD = d;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView fullName = (TextView)getActivity().findViewById(R.id.textFullName);
                        TextView promo = (TextView)getActivity().findViewById(R.id.textPromo);
                        TextView log = (TextView)getActivity().findViewById(R.id.textLogTime);
                        TextView gpa = (TextView)getActivity().findViewById(R.id.textGPA);
                        TextView loc = (TextView)getActivity().findViewById(R.id.textLocation);
                        TextView tel = (TextView)getActivity().findViewById(R.id.textPhone);
                        TextView mail = (TextView)getActivity().findViewById(R.id.textMail);

                        fullName.setText(goodName);
                        gpa.setText("GPA: \t" + goodGPA);
                        promo.setText("Promo: \t" + goodPromo);
                        loc.setText("Location: \t" + goodloc);
                        log.setText("Log: \t" + hours + " hour(s)");

                        tel.setVisibility(goodphone.equals("N/A") ? View.GONE : View.VISIBLE);
                        tel.setText("Phone: \t" + goodphone);
                        mail.setText(goodName.equals("Leeroy Jenkins") ? "" : "Mail: \t" + login + "@epitech.eu");

                        addExtraToTextViews(tel, mail, goodphone, login);
                        ImageView image = (ImageView) getActivity().findViewById(R.id.photo_trombi);
                        image.setImageDrawable(goodD);
                    }
                });
            }
        }).start();
    }

    private void addExtraToTextViews(TextView tel, TextView mail, final String goodphone, final String login) {
        if (!goodphone.equals("N/A")) {
            tel.setTextColor(Color.rgb(60, 60, 255));
            tel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", goodphone, null)));
                }
            });
        }

        if (mail.length() > 0) {
            mail.setTextColor(Color.rgb(60, 60, 255));
            mail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    String[] TO = {login + "@epitech.eu"};
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                }
            });
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
        void onFragmentInteraction(Uri uri);
    }
}
