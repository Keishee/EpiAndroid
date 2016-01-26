package com.example.joseph.app.helper;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Alex on 25/01/2016.
 */
public class MarkInfo {
    private String title;
    private String corrector;
    private String comment;
    private String marks;

    public MarkInfo(String title, String corrector, String comment, String marks) {
        this.title = title;
        this.corrector = corrector;
        this.comment = comment;
        this.marks = marks;
    }

    public String getTitle() { return title; }

    public String getCorrector() {
        return corrector;
    }

    public String getComment() {
        return comment;
    }

    public String getMarks() { return marks; }
}