package com.example.joseph.app.json;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by Clément on 25/01/2016.
 */
public class JsonGrabber {
    private static final String TAG = "JsonGrabber";

    public static <T> T getVariableAndCast(String response, String... args) {
        JsonParser parser = new JsonParser();
        if (response.isEmpty())
            return null;

        JsonElement elem = parser.parse(response);
        for (String token : args) {
            if (elem == null)
                break;
            if (elem.isJsonObject()) {
                elem = ((JsonObject)elem).get(token);
            } else if (elem.isJsonArray()) {
                JsonArray array = elem.getAsJsonArray();
                // todo: fct qui find le token dans un array
            } else {
                return (T)elem.getAsString();
            }
        }
        if (elem == null) {
            StringBuilder buffer = new StringBuilder();
            for (String s : args)
                buffer.append("\'" + s + "\'" + " ");
            buffer.deleteCharAt(buffer.length() - 1);
            Log.e(TAG, "path \"" + buffer.toString() + "\" is wrong / doesn't exist");
            return null;
        }
        return (T)elem.getAsString();
    }

    public static JsonArray getArrayFromPath(String response, String... args) {
        JsonParser parser = new JsonParser();
        if (response.isEmpty())
            return null;

        JsonElement elem = parser.parse(response);
        for (String token : args) {
            if (elem == null)
                break;
            if (elem.isJsonObject()) {
                elem = ((JsonObject) elem).get(token);
            } else if (elem.isJsonArray()) {
                JsonArray array = elem.getAsJsonArray();
                if (token == args[args.length - 1])
                    return array;
                // todo: appel fct Alex
            }
        }
        return null;
    }
}
