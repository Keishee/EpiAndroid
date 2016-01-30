package com.example.joseph.app.helper;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by Clément on 25/01/2016.
 */
public class ActiveUser {
    private String login = null;
    private String fullName = null;
    private String GPA = null;
    private Drawable userImage = null;
    private String logTime = null;
    private int semester = -1;
    private int promo = -1;
    private int studentYear = 1;
    private String location = null;

    public ActiveUser() {
    }

    public int getStudentYear() {
        return studentYear;
    }

    public void setStudentYear(int studentYear) {
        this.studentYear = studentYear;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPromo() {
        return promo;
    }

    public void setPromo(int promo) {
        this.promo = promo;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGPA() {
        return GPA;
    }

    public void setGPA(String GPA) {
        this.GPA = GPA;
    }

    public Drawable getUserImage() {
        return userImage;
    }

    public void setUserImage(Drawable userImage) {
        this.userImage = userImage;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }
}
