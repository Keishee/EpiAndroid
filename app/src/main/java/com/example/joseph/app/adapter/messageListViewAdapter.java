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
 * Created by Joseph on 23/01/2016.
 */
public class messageListViewAdapter extends BaseAdapter {

    private JsonArray jArray;
    private LayoutInflater inflater;

    public messageListViewAdapter(Context _context, JsonArray array) {
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
            vi = inflater.inflate(R.layout.custom_message_row, null);

        JsonObject obj = (JsonObject) jArray.get(position);
        String title = obj.get("title").getAsString();
        String content = obj.get("content").getAsString();
        String date = obj.get("date").getAsString();
        String teacher = ((JsonObject) obj.get("user")).get("title").getAsString();

        try {
            title = removeHyperlinkFromString(title);
            content = removeHyperlinkFromString(content);
        } catch (Exception e) {}

        ((TextView) vi.findViewById(R.id.titleView)).setText(title);
        ((TextView) vi.findViewById(R.id.contentView)).setText(content);
        ((TextView) vi.findViewById(R.id.teacherView)).setText(teacher);
        ((TextView) vi.findViewById(R.id.dateView)).setText(date);
        return vi;
    }

    private String removeHyperlinkFromString(String string) {
        while (string.contains("<a href")) {
            String linkOpen = string.substring(string.indexOf("<"), string.indexOf(">") + 1);
            string = string.replace(linkOpen, "$OPEN$");
            String hyperlink = string.substring(string.indexOf("$OPEN$") + 6, string.indexOf("<"));
            string = string.replace(hyperlink, "$LINK$");
            String linkClose = string.substring(string.indexOf("</"), string.indexOf(">") + 1);
            string = string.replace(linkClose, "$CLOSE$");
            string = string.replace("$OPEN$$LINK$$CLOSE$", hyperlink);
        }
        return string;
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
