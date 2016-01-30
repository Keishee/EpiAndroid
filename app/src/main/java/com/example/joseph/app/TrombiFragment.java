package com.example.joseph.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.example.joseph.app.adapter.trombiGridViewAdapter;
import com.example.joseph.app.helper.ActiveUser;
import com.example.joseph.app.helper.ApiIntra;
import com.example.joseph.app.json.JsonGrabber;
import com.google.gson.JsonArray;

import org.json.JSONArray;


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

    @Override
    public void onStart() {
        super.onStart();
        ActiveUser user = ((FrontPageActivity)getActivity()).getUser();
        ApiIntra.getListStudents(String.valueOf(user.getPromo()), user.getLocation());

        if (getActivity() == null)
            return;
        SharedPreferences prefs = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        String response = prefs.getString("students", null);
        JsonArray array = JsonGrabber.getArrayFromPath(response, "items");
        GridView trombiGridView = (GridView) getActivity().findViewById(R.id.trombiGridView);
        if (trombiGridView == null)
            return;
        trombiGridViewAdapter trombiCustomAdapter = new trombiGridViewAdapter(getActivity().getApplicationContext(), array);
        trombiGridView.setAdapter(trombiCustomAdapter);

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
