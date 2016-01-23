package com.example.joseph.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.gson.JsonArray;

import org.json.JSONArray;

/**
 * Created by Joseph on 23/01/2016.
 */
public class messageListViewAdapter extends BaseAdapter {

    private JsonArray jArray;

    public messageListViewAdapter(Context _context, JsonArray array) {
        jArray = array;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        /*ICI TU REMPLIS PARSE ET REMPLI LA LIST
            -> postion c la case que c'est en train de remplir
            -> convertView c la view de la case
         */

        return null;
    }

    @Override
    public long getItemId (int position) {
        return 0;
    }

    @Override
    public Object getItem (int position) {
        return null;
    }
}
