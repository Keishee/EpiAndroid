package com.example.joseph.app.helper;

import android.util.Log;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Joseph on 19/01/2016.
 */
public class ApiManager {

    public static String postApiCall(String module, String... args) {
        try {
            URL url = new URL("https://epitech-api.herokuapp.com/" + module);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            //conn.setReadTimeout(10000);
            //conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            String urlParameters = "";
            int k = 0;
            for (String arg : args) {
                if (k == 0) {
                    urlParameters += arg + "=";
                } else if (k % 2 == 0) {
                    urlParameters += "&" + arg + "=";
                } else {
                    urlParameters += URLEncoder.encode(arg, "UTF-8");
                }
                ++k;
            }

            DataOutputStream wr = new DataOutputStream(
                    conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            //Get Response
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append("\n");
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            Log.e("ApiManager", e.getMessage());
            return "";
        }
    }

    public static String getApiCall(String module, String... args) {
        try {
            //conn.setReadTimeout(10000);
            //conn.setConnectTimeout(15000);

            String urlParameter = "https://epitech-api.herokuapp.com/" + module + "?";
            int k = 0;
            for (String arg : args) {
                if (k % 2 == 0)
                    urlParameter += arg + "=";
                else
                    urlParameter += arg + "&";
                ++k;
            }

            Log.i("ApiManager", urlParameter);

            URL url = new URL(urlParameter);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            //Get Response
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append("\n");
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            Log.e("ApiManager", e.getMessage());
            return "";
        }
    }
}

