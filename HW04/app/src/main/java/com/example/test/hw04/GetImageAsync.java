package com.example.test.hw04;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetImageAsync extends AsyncTask<String, Void, Void> {

    ImageIData idata;

    Bitmap bitmap = null;
    boolean noImage;

    public GetImageAsync(ImageIData idata) {
        this.idata = idata;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        idata.imagePostExecute(bitmap);



    }

    @Override
    protected void onPreExecute() {

        idata.imagePreExecute();


    }

    @Override
    protected Void doInBackground(String... params) {
        noImage = false;

        if(params[0] != "") {

            HttpURLConnection connection = null;
            bitmap = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                }
                else{

                }
            } catch (IOException e) {
                //Handle the exceptions
            } finally {
                //Close open connection
            }
        }
        else{
            noImage = true;
        }
        return null;
    }

    public static interface ImageIData{
        public void imagePreExecute();
        public void imagePostExecute(Bitmap bitmap);
    }
}