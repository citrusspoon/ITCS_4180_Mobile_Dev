package com.example.test.hw04;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


class GetKeywordsAsync extends AsyncTask<String, Integer, ArrayList<String>> {


    KeywordIData idata;

    public GetKeywordsAsync(KeywordIData idata) {
        this.idata = idata;
    }

    @Override
    protected void onPostExecute(ArrayList<String> s) {

        idata.keywordPostExecute(s);


    }


    @Override
    protected ArrayList<String> doInBackground(String... params) {

        HttpURLConnection connection = null;
        ArrayList<String> result = new ArrayList<>();
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                JSONObject root = new JSONObject(json);
                JSONArray categories = root.getJSONArray("categories");

                for (int i=0;i<categories.length();i++) {

                    result.add(categories.getString(i));

                }
            }
        } catch (Exception e) {
            //Handle Exceptions
        } finally {
            //Close the connections
        }
        return result;
    }

    public static interface KeywordIData{
        public void keywordPostExecute(ArrayList<String> s);
    }

}