package com.example.test.hw03;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by citru_000 on 10/3/2017.
 */

public class GetImageAsync extends AsyncTask<String, Void, String> {

    ImageView questionImage;
    Bitmap bitmap;
    ProgressBar loadingBar;
    boolean noImage;
    TextView loadStatus;
    Button nextButton;

    public GetImageAsync(ImageView questionImage, ProgressBar loadingBar, TextView loadStatus, Button nextButton) {
        this.questionImage = questionImage;
        this.loadingBar = loadingBar;
        this.loadStatus = loadStatus;
        this.nextButton = nextButton;
    }

    @Override
    protected void onPreExecute() {
        loadingBar.setVisibility(View.VISIBLE);
        loadStatus.setVisibility(View.VISIBLE);
        questionImage.setVisibility(View.INVISIBLE);
        nextButton.setEnabled(false);
    }

    @Override
    protected void onPostExecute(String s) {

        if(!noImage)
            questionImage.setImageBitmap(bitmap);
        else{
            Log.d("test", "No Image");
            questionImage.setImageResource(R.drawable.no_image);
        }

        loadingBar.setVisibility(View.INVISIBLE);
        loadStatus.setVisibility(View.INVISIBLE);
        questionImage.setVisibility(View.VISIBLE);
        nextButton.setEnabled(true);
        Log.d("test", "Image Set");

    }

    @Override
    protected String doInBackground(String... strings) {

        noImage = false;

       // Log.d("test", "URL: " + strings[0]);

        if(strings[0] != "") {

            HttpURLConnection connection = null;
            bitmap = null;
            try {
                URL url = new URL(strings[0]);
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
}
