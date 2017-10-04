package com.example.test.hw03;

import android.content.Context;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by citru_000 on 10/3/2017.
 */

public class GetTriviaAsync extends AsyncTask<String, Void, String> {

    ArrayList<Question> questionList;
    Button startButton;
    ProgressBar loadingBar;
    TextView loadStatus;
    ImageView triviaImage;

    public GetTriviaAsync(ArrayList<Question> questionList, Button startButton, ProgressBar loadingBar, TextView loadStatus, ImageView triviaImage) {
        this.questionList = questionList;
        this.startButton = startButton;
        this.loadingBar = loadingBar;
        this.loadStatus = loadStatus;
        this.triviaImage = triviaImage;
        questionList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {

        Log.d("test", "Started getting trivia");
        loadingBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("test", "Finished getting trivia");
        loadingBar.setVisibility(View.INVISIBLE);
        loadStatus.setText("Trivia Ready");
        triviaImage.setVisibility(View.VISIBLE);
        startButton.setEnabled(true);


    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String result = null;
        try {
            URL url = new URL("http://dev.theappsdr.com/apis/trivia_json/trivia_text.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(' ');
                }
                result = stringBuilder.toString();
            }
        } catch (IOException e) {
            //Handle the exceptions
        } finally {
            //Close open connections and reader
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String[] temp = result.split("(?=\\s\\d+\\;)");

        //remove beginning ;
        for(int i = 1; i < temp.length; i++){
            temp[i] = temp[i].substring(1);
        }


        for(int j = 0; j < temp.length; j++) {
            questionList.add(new Question(temp[j]));
            //Log.d("test", "Added question: " + j);
        }


        return null;
    }


}
