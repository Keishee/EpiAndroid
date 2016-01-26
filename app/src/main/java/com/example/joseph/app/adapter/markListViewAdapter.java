package com.example.joseph.app.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.joseph.app.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;

/**
 * Created by Alex on 25/01/2016.
 */
public class markListViewAdapter extends BaseAdapter {

    private JsonArray jArray;
    private LayoutInflater inflater;

    public markListViewAdapter(Context _context, JsonArray array) {
        jArray = array;
        inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return jArray.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.custom_message_row_grade_marks, null);

        JsonObject obj = (JsonObject) jArray.get(position);
        if (obj != null) {
            try {
                String title = obj.get("title").getAsString();
                String comment = obj.get("comment").getAsString();
                String mark = obj.get("final_note").getAsString();
                String correcteur = obj.get("correcteur").getAsString();
                ((TextView) vi.findViewById(R.id.TitleMark)).setText(title);
                ((TextView) vi.findViewById(R.id.MarkMark)).setText(mark);
                ((TextView) vi.findViewById(R.id.AuthorMark)).setText(correcteur);
                ((TextView) vi.findViewById(R.id.CommentMark)).setText(comment);
            }catch (Exception e) {}
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
