package com.example.joseph.app.helper;

/**
 * Created by Clément on 25/01/2016.
 */
public class PlanningInfo {
    public String title;
    public String module;
    public int semester;
    public String room;
    public String start;
    public String end;

    public PlanningInfo(String title, String module, int semester, String room, String start, String end) {
        this.title = title;
        this.module = module;
        this.semester = semester;
        this.room = room;
        this.start = start;
        this.end = end;
    }

    public String getRoom(String fullRoom) {
        return fullRoom.substring(room.lastIndexOf("/"));
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