package com.example.joseph.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.joseph.app.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by Joseph on 30/01/2016.
 */
public class projectListViewAdapter extends BaseAdapter {
    private JsonArray jArray;
    private LayoutInflater inflater;

    public projectListViewAdapter(Context _context, JsonArray array) {
        jArray = array;
        inflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return jArray.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.custom_project_row, parent, false);

        JsonObject obj = (JsonObject) jArray.get(position);

        String title = obj.get("project_title").getAsString();

        ((TextView) vi.findViewById(R.id.projectTitle)).setText(title);

        if (obj.get("is_note").getAsBoolean()) {
            JsonElement note = obj.get("note");
            if (note.isJsonNull())
                return vi;
            String noteTxt = note.getAsString();
            ((TextView) vi.findViewById(R.id.noteTextView)).setText(noteTxt);
        }

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
