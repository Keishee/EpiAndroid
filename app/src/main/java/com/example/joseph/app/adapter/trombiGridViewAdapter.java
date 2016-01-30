package com.example.joseph.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joseph.app.R;
import com.google.gson.JsonArray;

/**
 * Created by Clément on 30/01/2016.
 */
public class trombiGridViewAdapter extends BaseAdapter {
    private JsonArray jArray;
    private Context context;
    private LayoutInflater inflater;

    public trombiGridViewAdapter(Context _context, JsonArray array) {
        jArray = array;
        context = _context;
        inflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return jArray.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            TextView txt = new TextView(context);
            txt.setText("Hello");
            layout.addView(txt);

            TextView txt2 = new TextView(context);
            txt2.setText("Hello Again");
            layout.addView(txt2);

            layout.setLayoutParams(new GridView.LayoutParams((position / 3) - 1,(position % 3) - 1));
        }
        return layout;
    }
}
