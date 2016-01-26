package com.example.joseph.app.helper;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Clément on 25/01/2016.
 */
public class PlanningInfo {
    private String title;
    private String module;
    private int semester;
    private String room;
    private String start;
    private String end;
    private Date startDate;
    private Date endDate;
    boolean registered;
    boolean allowToken;

    // requested by token POST
    private String scolaryear;
    private String codemodule;
    private String codeinstance;
    private String codeacti;
    private String codeevent;

    public PlanningInfo(String title, String module, int semester, String room, String start, String end, boolean registered) {
        this.title = title;
        this.module = module;
        this.semester = semester;
        this.room = room;
        this.start = start;
        this.end = end;
        this.registered = registered;
        ParsePosition pos = new ParsePosition(0);
        DateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        startDate = simpledateformat.parse(start, pos);
        pos.setIndex(0);
        endDate = simpledateformat.parse(end, pos);
    }

    public void addTokenRequestedInfos(String scolaryear, String codemodule, String codeinstance, String codeacti, String codeevent, boolean allowToken) {
        this.scolaryear = scolaryear;
        this.codemodule = codemodule;
        this.codeinstance = codeinstance;
        this.codeacti = codeacti;
        this.codeevent = codeevent;
        this.allowToken = allowToken;
    }

    public boolean allowToken() {
        return allowToken;
    }

    public String getScolaryear() {
        return scolaryear;
    }

    public String getCodemodule() {
        return codemodule;
    }

    public String getCodeinstance() {
        return codeinstance;
    }

    public String getCodeacti() {
        return codeacti;
    }

    public String getCodeevent() {
        return codeevent;
    }

    public String getRoom() {
        return getRoom(room);
    }

    public String getNameOfDay() {
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        return outFormat.format(startDate);     }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getDuration() {
        long diff = endDate.getTime() - startDate.getTime();
        Date duration = new Date(diff);
        return duration;
    }

    public boolean isRegistered() {
        return registered;
    }

    public String getRoom(String fullRoom) {
        return fullRoom.substring(room.lastIndexOf("/") + 1);
    }

    public String getEnd() {
        return end;
    }

    public String getStart() {
        return start;
    }

    public String getTitle() {
        return title;
    }

    public String getModule() {
        return module;
    }

    public int getSemester() {
        return semester;
    }

}