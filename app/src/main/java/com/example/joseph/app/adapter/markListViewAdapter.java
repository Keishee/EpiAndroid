package com.example.joseph.app.adapter;

import android.app.PendingIntent;
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

import com.example.joseph.app.FrontPageActivity;
import com.example.joseph.app.R;
import com.example.joseph.app.helper.MarkInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Alex on 25/01/2016.
 */
public class markListViewAdapter extends BaseAdapter {

    private ArrayList<MarkInfo> jArray;
    private LayoutInflater inflater;
    private String user;

    public markListViewAdapter(Context _context, ArrayList<MarkInfo> array) {
        jArray = array;
        inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            vi = inflater.inflate(R.layout.custom_message_row_grade_marks, null);

        if (jArray != null) {
            try {
                String title = jArray.get(position).getTitle();
                String comment = jArray.get(position).getComment();
                String mark = jArray.get(position).getMarks();
                String correcteur = jArray.get(position).getCorrector();
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
