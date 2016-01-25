package com.example.joseph.app.helper;

import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import com.example.joseph.app.R;
import com.example.joseph.app.adapter.messageListViewAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Joseph on 19/01/2016.
 */
public class ApiManager {

    public static String postApiCall(String module, String... args) {
        final String module2 = module;
        final String[] args2 = args;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> result = executor.submit(new Callable<String>() {
            public String call() throws Exception {
                try {
                    URL url = new URL("https://epitech-api.herokuapp.com/" + module2);
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    //conn.setReadTimeout(10000);
                    //conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);


                    String urlParameters = "";
                    int k = 0;
                    for (String arg : args2) {
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
        });
        try {
            return result.get();
        } catch (Exception e) {
            //handle exception
            Log.e("postApiCall", e.getMessage());
            return "";
        }
    }

    public static String getApiCall(String module, String... args) {
        String urlParameterTmp = "https://epitech-api.herokuapp.com/" + module + "?";
        int k = 0;
        for (String arg : args) {
            if (k % 2 == 0)
                urlParameterTmp += arg + "=";
            else
                urlParameterTmp += arg + "&";
            ++k;
        }
        final String urlParameter = urlParameterTmp;

        Log.i("ApiManager", urlParameter);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> result = executor.submit(new Callable<String>() {
            public String call() throws Exception {
                try {


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
        });
        try {
            return result.get();
        } catch (Exception e) {
            //handle exception
            Log.e("getApiCall", e.getMessage());
            return "";
        }

    }
}


