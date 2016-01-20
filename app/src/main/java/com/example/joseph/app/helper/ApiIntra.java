package com.example.joseph.app.helper;

/**
 * Created by Alex on 20/01/2016.
 */
public class ApiIntra {

    private String _token;

    public String postToken(String name, String password) {
        return ApiManager.postApiCall("login", name, "password", password);
    }

    public void setToken(String token) {
        _token = token;
    }

    public String getInfos() {
        return ApiManager.getApiCall("infos", "token", _token);
    }

    /**
     * "start" : "YYYY-MM-DD"
     * "end" : "YYYY-MM-DD"
     *
    **/
    public String getPlanning(String start, String end) {
        return ApiManager.getApiCall("planning", "token", _token, start, end);
    }

    /**
     * "start" : "YYYY-MM-DD"
     * "end" : "YYYY-MM-DD"
     *
     **/
    public String getSusies(String start, String end) {
        return ApiManager.getApiCall("susies", "token", _token, "start", start, "end", end);
    }

    /**
     * "id" : "6301"
     * "calendar_id" : "42"
     *
    **/
    public String getSusie(String id, String calendar_id) {
        return ApiManager.getApiCall("susies", "token", _token, "id", id, "calendar_id", calendar_id);
    }

    /**
    * "id" : "6301"
    * "calendar_id" : "42"
     *
     **/
    public String subscribeSusie(String id, String calendar_id) {
        return ApiManager.postApiCall("susies", "token", _token, "id", id, "calendar_id", calendar_id);
    }

    /**
     * "id" : "6301"
     * "calendar_id" : "42"
     *
     **/
    public String unsuscribeSusie(String id, String calendar_id) {
        //return ApiManager.postApiCall("susies", "token", _token, "id", id, "calendar_id", calendar_id); ----> DELETE request .. ?
        return "";
    }

    public String getProjects() {
        return ApiManager.getApiCall("projects", "token", _token);
    }

    /**
     * "schoolyear" : 2014
     * "codemodule":"B-GPR-360-0",
     * "codeinstance":"PAR-6-1",
     * "codeacti":"acti-167486"
     *
     **/
    public String getProject(String schoolYear, String codeModule, String codeInstance, String codeActi) {
        return ApiManager.getApiCall("project", "token", _token, "schoolyear", schoolYear, "codemodule", codeModule, "codeinstance", codeInstance, "codeacti", codeActi);
    }

    /**
     * "schoolyear" : 2014
     * "codemodule":"B-GPR-360-0",
     * "codeinstance":"PAR-6-1",
     * "codeacti":"acti-167486"
     *
     **/
    public String subscribeProject(String schoolYear, String codeModule, String codeInstance, String codeActi) {
        return ApiManager.postApiCall("project", "token", _token, "schoolyear", schoolYear, "codemodule", codeModule, "codeinstance", codeInstance, "codeacti", codeActi);
    }

    /**
     * "schoolyear" : 2014
     * "codemodule":"B-GPR-360-0",
     * "codeinstance":"PAR-6-1",
     * "codeacti":"acti-167486"
     *
     **/
    public String unsubscribeProject(String schoolYear, String codeModule, String codeInstance, String codeActi) {
        //return ApiManager.postApiCall("project", "token", _token, "schoolyear", schoolYear, "codemodule", codeModule, "codeinstance", codeInstance, "codeacti", codeActi); ---> DELETE request ?
        return "";
    }

    /**
     * "scolaryear":2014,
     * "codemodule":"B-GPR-360-0",
     * "codeinstance":"PAR-6-1",
     * "codeacti":"acti-167486"
     *
     **/
    public String getProjectFiles(String schoolYear, String codeModule, String codeInstance, String codeActi) {
        return ApiManager.getApiCall("project/files", "token", _token, "schoolyear", schoolYear, "codemodule", codeModule, "codeinstance", codeInstance, "codeacti", codeActi);
    }

    public String getModules() {
        return ApiManager.getApiCall("modules", "token", _token);
    }

    /**
     * "scolaryear":2014,
     * "location":"FR/PAR" or another location in "FR/BDX","FR/LIL","FR/LYN","FR/MAR","FR/MPL","FR/NCY","FR/NAN","FR/NCE","FR/PAR","FR/REN","FR/STG","FR/TLS"
     * "course":"bachelor/classic" or "bachelor/tek1ed" or "bachelor/tek2ed"
     *
     */
    public String getAllModules(String scolarYear, String location, String course) {
        return ApiManager.getApiCall("allmodules", "token", _token, "scolaryear", scolarYear, "location", location, "course", course);
    }

    /**
     * "scolaryear":2014,
     * "codemodule":"B-GPR-360-0",
     * "codeinstance":"PAR-6-1"
     *
     */
    public String getModule(String scolarYear, String codeModule, String codeInstance) {
        return ApiManager.getApiCall("module", "token", _token, "scolaryear", scolarYear, "codemodule", codeModule, "codeinstance", codeInstance);
    }

    /**
     * "scolaryear":2014,
     * "codemodule":"B-GPR-360-0",
     * "codeinstance":"PAR-6-1"
     *
     */
    public String subscribeModule(String scolarYear, String codeModule, String codeInstance) {
        return ApiManager.postApiCall("module", "token", _token, "scolaryear", scolarYear, "codemodule", codeModule, "codeinstance", codeInstance);
    }

    /**
     * "scolaryear":2014,
     * "codemodule":"B-GPR-360-0",
     * "codeinstance":"PAR-6-1"
     *
     */
    public String unsubscribeModule(String scolarYear, String codeModule, String codeInstance) {
        //return ApiManager.postApiCall("module", "token", _token, "scolaryear", scolarYear, "codemodule", codeModule, "codeinstance", codeInstance); ---> DELETE request ?
        return "";
    }

    /**
     * "scolaryear":2014,
     * "codemodule":"B-PAV-590",
     * "codeinstance":"NAN-5-1",
     * "codeacti":"acti-164603"
     * "codeevent":"event-173408"
     *
     */
    public String getEvent(String scolarYear, String codeModule, String codeInstance, String codeActi, String codeEvent) {
        return ApiManager.getApiCall("event", "token", _token, "scolaryear", scolarYear, "codemodule", codeModule, "codeinstance", codeInstance, "codeacti", codeActi, "codeevent", codeEvent);
    }

    /**
     * "scolaryear":2014,
     * "codemodule":"B-PAV-590",
     * "codeinstance":"NAN-5-1",
     * "codeacti":"acti-164603"
     * "codeevent":"event-173408"
     *
     */
    public String subscribeEvent(String scolarYear, String codeModule, String codeInstance, String codeActi, String codeEvent) {
        return ApiManager.postApiCall("event", "token", _token, "scolaryear", scolarYear, "codemodule", codeModule, "codeinstance", codeInstance, "codeacti", codeActi, "codeevent", codeEvent);
    }

    /**
     * "scolaryear":2014,
     * "codemodule":"B-PAV-590",
     * "codeinstance":"NAN-5-1",
     * "codeacti":"acti-164603"
     * "codeevent":"event-173408"
     *
     */
    public String unsubscribeEvent(String scolarYear, String codeModule, String codeInstance, String codeActi, String codeEvent) {
        //return ApiManager.postApiCall("event", "token", _token, "scolaryear", scolarYear, "codemodule", codeModule, "codeinstance", codeInstance, "codeacti", codeActi, "codeevent", codeEvent);
        return "";
    }

    public String getMarks() {
        return ApiManager.getApiCall("marks", "token", _token);
    }

    public String getMessages() {
        return ApiManager.getApiCall("messages", "token", _token);
    }

    public String getAlerts() {
        return ApiManager.getApiCall("alerts", "token", _token);
    }

    /**
     * "login":"login_x"
     *
     */
    public String getPhoto(String login) {
        return ApiManager.getApiCall("photo", "token", _token, "login", login);
    }

    /**
     * "scolaryear":2014,
     * "codemodule":"B-GPR-360-0",
     * "codeinstance":"PAR-6-1",
     * "codeacti":"acti-167486",
     * "codeevent":"event-177013"
     * "tokenvalidationcode":"00000000"
     *
     */
    public String valideToken(String scolarYear, String codeModule, String codeInstance, String codeActi, String codeEvent, String token) {
        return ApiManager.postApiCall("token", "token", _token, "scolaryear", scolarYear, "codemodule", codeModule, "codeinstance", codeInstance, "codeacti", codeActi, "codeevent", codeEvent, "tokenvalidationcode", token);
    }

    /**
     * "year":2014
     * "location":"FR/PAR" or another location in "FR/BDX","FR/LIL","FR/LYN","FR/MAR","FR/MPL","FR/NCY","FR/NAN","FR/NCE","FR/PAR","FR/REN","FR/STG","FR/TLS"
     * "course":"bachelor/classic" or "bachelor/tek1ed" or "bachelor/tek2ed"
     * "promo":"tek3"
     * "offset":43
     *
     */
    public String getListStudents(String year, String location, String course, String promo, String offset) {
        return ApiManager.getApiCall("trombi", "token", _token, "year", year, "location", location, "couse", course, "promo", promo, "offset", offset);
    }

    /**
     * "login":"login_x"
     *
     */
    public String getUser(String login) {
        return ApiManager.getApiCall("user", "token", _token, "login", login);
    }
}
