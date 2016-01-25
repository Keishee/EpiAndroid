package com.example.joseph.app.helper;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by Alex on 20/01/2016.
 */
public class ApiIntra {

    private static String _token;
    private static AppCompatActivity activity;

    public static void setActivity(AppCompatActivity act) {
        activity = act;
    }

    public static void postToken(String name, String password) {
        String tmp = ApiManager.postApiCall("login", "login", name, "password", password);
        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject) parser.parse(tmp);
        _token = object.get("token").getAsString();
        Log.i("ApiIntra", _token);
    }

    public static void setToken(String token) {
        _token = token;
    }

    public static void getInfos() {
        String tmp = ApiManager.getApiCall("infos", "token", _token);
        SharedPreferences.Editor editor = activity.getPreferences(activity.MODE_PRIVATE).edit();
        editor.putString("infos", tmp);
        editor.commit();
    }

    /**
     * "start" : "YYYY-MM-DD"
     * "end" : "YYYY-MM-DD"
     **/
    public static void getPlanning(String start, String end) {
        String tmp = ApiManager.getApiCall("planning", "token", _token, "start", start, "end", end);
        SharedPreferences.Editor editor = activity.getPreferences(activity.MODE_PRIVATE).edit();
        editor.putString("planning", tmp);
        editor.commit();
    }

    /**
     * "start" : "YYYY-MM-DD"
     * "end" : "YYYY-MM-DD"
     **/
    public static void getSusies(String start, String end) {
        String tmp = ApiManager.getApiCall("susies", "token", _token, "start", start, "end", end);
        SharedPreferences.Editor editor = activity.getPreferences(activity.MODE_PRIVATE).edit();
        editor.putString("susies", tmp);
        editor.commit();
    }

    /**
     * "id" : "6301"
     * "calendar_id" : "42"
     **/
    public static void getSusie(String id, String calendar_id) {
        String tmp = ApiManager.getApiCall("susies", "token", _token, "id", id, "calendar_id", calendar_id);
        SharedPreferences.Editor editor = activity.getPreferences(activity.MODE_PRIVATE).edit();
        editor.putString("susie", tmp);
        editor.commit();
    }

    /**
     * "id" : "6301"
     * "calendar_id" : "42"
     **/
    public static void subscribeSusie(String id, String calendar_id) {
        ApiManager.postApiCall("susies", "token", _token, "id", id, "calendar_id", calendar_id);
    }

    /**
     * "id" : "6301"
     * "calendar_id" : "42"
     **/
    public static void unsuscribeSusie(String id, String calendar_id) {
        //return ApiManager.postApiCall("susies", "token", _token, "id", id, "calendar_id", calendar_id); ----> DELETE request .. ?
        String tmp = "";
    }

    public static void getProjects() {
        String tmp = ApiManager.getApiCall("projects", "token", _token);
        SharedPreferences.Editor editor = activity.getPreferences(activity.MODE_PRIVATE).edit();
        editor.putString("projects", tmp);
        editor.commit();
    }

    /**
     * "schoolyear" : 2014
     * "codemodule":"B-GPR-360-0",
     * "codeinstance":"PAR-6-1",
     * "codeacti":"acti-167486"
     **/
    public static String getProject(String schoolYear, String codeModule, String codeInstance, String codeActi) {
        return ApiManager.getApiCall("project", "token", _token, "schoolyear", schoolYear, "codemodule", codeModule, "codeinstance", codeInstance, "codeacti", codeActi);
    }

    /**
     * "schoolyear" : 2014
     * "codemodule":"B-GPR-360-0",
     * "codeinstance":"PAR-6-1",
     * "codeacti":"acti-167486"
     **/
    public static String subscribeProject(String schoolYear, String codeModule, String codeInstance, String codeActi) {
        return ApiManager.postApiCall("project", "token", _token, "schoolyear", schoolYear, "codemodule", codeModule, "codeinstance", codeInstance, "codeacti", codeActi);
    }

    /**
     * "schoolyear" : 2014
     * "codemodule":"B-GPR-360-0",
     * "codeinstance":"PAR-6-1",
     * "codeacti":"acti-167486"
     **/
    public static void unsubscribeProject(String schoolYear, String codeModule, String codeInstance, String codeActi) {
        //return ApiManager.postApiCall("project", "token", _token, "schoolyear", schoolYear, "codemodule", codeModule, "codeinstance", codeInstance, "codeacti", codeActi); ---> DELETE request ?
        String tmp = "";
    }

    /**
     * "scolaryear":2014,
     * "codemodule":"B-GPR-360-0",
     * "codeinstance":"PAR-6-1",
     * "codeacti":"acti-167486"
     **/
    public static String getProjectFiles(String schoolYear, String codeModule, String codeInstance, String codeActi) {
        return ApiManager.getApiCall("project/files", "token", _token, "schoolyear", schoolYear, "codemodule", codeModule, "codeinstance", codeInstance, "codeacti", codeActi);
    }

    public static void getModules() {
        String tmp = ApiManager.getApiCall("modules", "token", _token);
        SharedPreferences.Editor editor = activity.getPreferences(activity.MODE_PRIVATE).edit();
        editor.putString("modules", tmp);
        editor.commit();
    }

    /**
     * "scolaryear":2014,
     * "location":"FR/PAR" or another location in "FR/BDX","FR/LIL","FR/LYN","FR/MAR","FR/MPL","FR/NCY","FR/NAN","FR/NCE","FR/PAR","FR/REN","FR/STG","FR/TLS"
     * "course":"bachelor/classic" or "bachelor/tek1ed" or "bachelor/tek2ed"
     */
    public static void getAllModules(String scolarYear, String location, String course) {
        String tmp = ApiManager.getApiCall("allmodules", "token", _token, "scolaryear", scolarYear, "location", location, "course", course);
        SharedPreferences.Editor editor = activity.getPreferences(activity.MODE_PRIVATE).edit();
        editor.putString("allModules", tmp);
        editor.commit();
    }

    /**
     * "scolaryear":2014,
     * "codemodule":"B-GPR-360-0",
     * "codeinstance":"PAR-6-1"
     */
    public static String getModule(String scolarYear, String codeModule, String codeInstance) {
        return ApiManager.getApiCall("module", "token", _token, "scolaryear", scolarYear, "codemodule", codeModule, "codeinstance", codeInstance);
    }

    /**
     * "scolaryear":2014,
     * "codemodule":"B-GPR-360-0",
     * "codeinstance":"PAR-6-1"
     */
    public static void subscribeModule(String scolarYear, String codeModule, String codeInstance) {
        ApiManager.postApiCall("module", "token", _token, "scolaryear", scolarYear, "codemodule", codeModule, "codeinstance", codeInstance);
    }

    /**
     * "scolaryear":2014,
     * "codemodule":"B-GPR-360-0",
     * "codeinstance":"PAR-6-1"
     */
    public static void unsubscribeModule(String scolarYear, String codeModule, String codeInstance) {
        //return ApiManager.postApiCall("module", "token", _token, "scolaryear", scolarYear, "codemodule", codeModule, "codeinstance", codeInstance); ---> DELETE request ?
        String tmp = "";
    }

    /**
     * "scolaryear":2014,
     * "codemodule":"B-PAV-590",
     * "codeinstance":"NAN-5-1",
     * "codeacti":"acti-164603"
     * "codeevent":"event-173408"
     */
    public static String getEvent(String scolarYear, String codeModule, String codeInstance, String codeActi, String codeEvent) {
        return ApiManager.getApiCall("event", "token", _token, "scolaryear", scolarYear, "codemodule", codeModule, "codeinstance", codeInstance, "codeacti", codeActi, "codeevent", codeEvent);
    }

    /**
     * "scolaryear":2014,
     * "codemodule":"B-PAV-590",
     * "codeinstance":"NAN-5-1",
     * "codeacti":"acti-164603"
     * "codeevent":"event-173408"
     */
    public static void subscribeEvent(String scolarYear, String codeModule, String codeInstance, String codeActi, String codeEvent) {
        ApiManager.postApiCall("event", "token", _token, "scolaryear", scolarYear, "codemodule", codeModule, "codeinstance", codeInstance, "codeacti", codeActi, "codeevent", codeEvent);
    }

    /**
     * "scolaryear":2014,
     * "codemodule":"B-PAV-590",
     * "codeinstance":"NAN-5-1",
     * "codeacti":"acti-164603"
     * "codeevent":"event-173408"
     */
    public static void unsubscribeEvent(String scolarYear, String codeModule, String codeInstance, String codeActi, String codeEvent) {
        //return ApiManager.postApiCall("event", "token", _token, "scolaryear", scolarYear, "codemodule", codeModule, "codeinstance", codeInstance, "codeacti", codeActi, "codeevent", codeEvent);
        String tmp = "";
    }

    public static void getMarks() {
        String tmp = ApiManager.getApiCall("marks", "token", _token);
        SharedPreferences.Editor editor = activity.getPreferences(activity.MODE_PRIVATE).edit();
        editor.putString("marks", tmp);
        editor.commit();
    }

    public static void getMessages() {
        String tmp = ApiManager.getApiCall("messages", "token", _token);
        SharedPreferences.Editor editor = activity.getPreferences(activity.MODE_PRIVATE).edit();
        editor.putString("messages", tmp);
        editor.commit();
    }

    public static void getAlerts() {
        String tmp = ApiManager.getApiCall("alerts", "token", _token);
        SharedPreferences.Editor editor = activity.getPreferences(activity.MODE_PRIVATE).edit();
        editor.putString("alerts", tmp);
        editor.commit();
    }

    /**
     * "login":"login_x"
     */
    public static void getPhoto(String login) {
        String tmp = ApiManager.getApiCall("photo", "token", _token, "login", login);
        SharedPreferences.Editor editor = activity.getPreferences(activity.MODE_PRIVATE).edit();
        editor.putString("photo", tmp);
        editor.commit();
    }

    /**
     * "scolaryear":2014,
     * "codemodule":"B-GPR-360-0",
     * "codeinstance":"PAR-6-1",
     * "codeacti":"acti-167486",
     * "codeevent":"event-177013"
     * "tokenvalidationcode":"00000000"
     */
    public static void valideToken(String scolarYear, String codeModule, String codeInstance, String codeActi, String codeEvent, String token) {
        ApiManager.postApiCall("token", "token", _token, "scolaryear", scolarYear, "codemodule", codeModule, "codeinstance", codeInstance, "codeacti", codeActi, "codeevent", codeEvent, "tokenvalidationcode", token);
    }

    /**
     * "year":2014
     * "location":"FR/PAR" or another location in "FR/BDX","FR/LIL","FR/LYN","FR/MAR","FR/MPL","FR/NCY","FR/NAN","FR/NCE","FR/PAR","FR/REN","FR/STG","FR/TLS"
     * "course":"bachelor/classic" or "bachelor/tek1ed" or "bachelor/tek2ed"
     * "promo":"tek3"
     * "offset":43
     */
    public static void getListStudents(String year, String location, String course, String promo, String offset) {
        String tmp = ApiManager.getApiCall("trombi", "token", _token, "year", year, "location", location, "couse", course, "promo", promo, "offset", offset);
        SharedPreferences.Editor editor = activity.getPreferences(activity.MODE_PRIVATE).edit();
        editor.putString("students", tmp);
        editor.commit();
    }

    /**
     * "login":"login_x"
     */
    public static String getUser(String login) {
        return ApiManager.getApiCall("user", "token", _token, "user", login);
    }
}
