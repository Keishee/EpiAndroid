package com.example.joseph.app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.joseph.app.FrontPageActivity;
import com.example.joseph.app.R;
import com.example.joseph.app.helper.ApiIntra;
import com.example.joseph.app.helper.PlanningInfo;
import com.google.gson.JsonArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Clément on 25/01/2016.
 */
public class planningListViewAdapter extends BaseAdapter {
    private ArrayList<PlanningInfo> planningInfos;
    private LayoutInflater inflater;
    private Context context;
    private boolean onlyRegistered = false;

    public planningListViewAdapter(Context _context, ArrayList<PlanningInfo> planningInfos) {
        this.planningInfos = planningInfos;
        context = _context;
        inflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setPlanningInfos(ArrayList<PlanningInfo> planningInfos, boolean registered) {
        this.planningInfos = planningInfos;
        this.onlyRegistered = registered;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.custom_planning_row, null);

        final PlanningInfo pi = planningInfos.get(position);
        ((TextView) vi.findViewById(R.id.planningModule)).setText(pi.getTitle());
        ((TextView) vi.findViewById(R.id.planningCourse)).setText(pi.getModule());
        ((TextView) vi.findViewById(R.id.planningRoom)).setText(pi.getRoom());

        try {
            Date start = pi.getStartDate();
            Date end = pi.getEndDate();
            DateFormat format = new SimpleDateFormat("d\tMMMM\tHH:mm");
            ((TextView) vi.findViewById(R.id.planningStart)).setText(format.format(start));
            ((TextView) vi.findViewById(R.id.planningEnd)).setText(format.format(end));
        } catch (Exception e) {
        }

        if (onlyRegistered && pi.allowToken()) {
            createTokenField(vi, pi);
        }
        return vi;
    }

    private void createTokenField(View vi, final PlanningInfo pi) {
        Button button = (Button) (vi.findViewById(R.id.tokenButton));
        button.setVisibility(View.VISIBLE);
        final EditText et = (EditText) (vi.findViewById(R.id.tokenField));
        et.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String token = et.getText().toString();
                if (token.length() != 8)
                    return;
                final Handler handler = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ApiIntra.valideToken(pi.getScolaryear(), pi.getCodemodule(), pi.getCodeinstance(), pi.getCodeacti(), pi.getCodeevent(), token);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(context, "Token sent", Toast.LENGTH_SHORT);
                                toast.setText("Token sent");
                                toast.show();
                                et.getText().clear();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    @Override
    public int getCount() {
        return planningInfos.size();
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
