package com.example.test.hw04;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

class GetURLAsync extends AsyncTask<String, Integer, ArrayList<String>> {

    URLIData idata;
    String currentKeyword;

    public GetURLAsync(URLIData idata, String currentKeyword) {
        this.idata = idata;
        this.currentKeyword = currentKeyword;
    }

    @Override
    protected void onPostExecute(ArrayList<String> s) {

        idata.urlPostExecute(s);

    }




    @Override
    protected ArrayList<String> doInBackground(String... strings) {


        HttpURLConnection connection = null;
        ArrayList<String> result = new ArrayList<>();

        try {
            String strUrl = "http://dev.theappsdr.com/apis/photos/index.php" + "?" +
                    "keyword=" + URLEncoder.encode(currentKeyword, "UTF-8") + "&format=json";
            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                JSONObject root = new JSONObject(json);
                JSONArray urls = root.getJSONArray("urls");

                for (int i=0;i<urls.length();i++) {

                    result.add(urls.getString(i));

                }
            }
        } catch (IOException e) {
            //Handle the exceptions
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            //Close open connections and reader
        }
        return result;


    }

    public static interface URLIData{
        public void urlPostExecute(ArrayList<String> s);
    }
}