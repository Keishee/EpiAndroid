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

    public static String getVariable(String response, String... args) {
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
                JsonArray array = (JsonArray)elem.getAsJsonArray();
                elem = getVariableFromArray(array, token);
            } else {
                return elem.getAsString();
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
        return elem.getAsString();
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
                elem = getVariableFromArray(array, token);
            }
        }
        if (elem == null)
            return null;
        return elem.isJsonArray() ? (JsonArray)elem : null;
    }

    private static JsonElement getVariableFromArray(JsonArray array, String token) {
        for (int i = 0; i < array.size(); i++) {
            JsonElement element = null;
            JsonObject object = null;
            object = array.get(i).getAsJsonObject();

            if (object != null) {
                element = object.get(token);
                if (element != null)
                    return element;
            }
        }
        return null;
    }
}
