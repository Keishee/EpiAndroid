package com.example.joseph.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.joseph.app.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Joseph on 25/01/2016.
 */
public class semesterListViewAdapter extends BaseAdapter {
    private ArrayList<String> jArray;
    private LayoutInflater inflater;

    public semesterListViewAdapter(Context _context, ArrayList<String> array) {
        jArray = array;
        inflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (jArray != null)
            return jArray.size();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.custom_module_row, null);

        String semestre = jArray.get(position);
        ((TextView) vi.findViewById(R.id.moduleTitle)).setText(semestre);
        return vi;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }
}
