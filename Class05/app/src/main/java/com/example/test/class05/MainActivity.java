/*
* In Class 05
*
* Scott Schreiber, Brianna Kirkpatrick
*
* Group5_InClass05.zip
*
* */


package com.example.test.class05;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    CharSequence[] keywords;
    TextView photoName;
    ProgressBar progressBar;
    ImageView photoView;
    AlertDialog alert;
    int currentImageIndex;
    ArrayList<String> currentURLs;
    String currentKeyword;
    Button goButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //----------------------Elements-------------------------------//

        currentURLs = new ArrayList<>();
        currentImageIndex = 0;
        ImageView nextButton = (ImageView) findViewById(R.id.nextButton);
        ImageView prevButton = (ImageView) findViewById(R.id.prevButton);
        photoView = (ImageView) findViewById(R.id.photoView);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);

        photoName = (TextView) findViewById(R.id.photoNameView);
        goButton = (Button) findViewById(R.id.goButton);
        goButton.setEnabled(false);

        new GetKeywordsAsync().execute();



        //----------------------Listeners------------------------------//
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentImageIndex == currentURLs.size()-1)
                    currentImageIndex = -1;

                currentImageIndex +=1;
                new GetImageAsync().execute(currentURLs.get(currentImageIndex));
                Log.d("test",currentURLs.get(currentImageIndex));


            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(currentImageIndex == 0)
                    currentImageIndex = currentURLs.size();

                currentImageIndex -=1;
                new GetImageAsync().execute(currentURLs.get(currentImageIndex));

            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.show();
            }
        });


    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }


    class GetKeywordsAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPostExecute(String s) {

            Log.d("test", "Retrieved keywords");
            String[] temp = s.split(";");
            keywords = new CharSequence[temp.length];

            for (int i = 0; i < keywords.length; i++)
                keywords[i] = temp[i];


            progressBar.setVisibility(View.INVISIBLE);
            goButton.setEnabled(true);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Select Keyword")
                    .setItems(keywords, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {

                            photoName.setText(keywords[which]);
                            currentKeyword = photoName.getText().toString();
                            new GetURLAsync().execute();

                        }
                    });
            alert = builder.create();


        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... strings) {

            publishProgress(0);

            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String result = null;
            try {
                URL url = new URL("http://dev.theappsdr.com/apis/photos/keywords.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
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


            return result;
        }
    }


    class GetURLAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPostExecute(String s) {


            currentURLs.clear();
            String[] temp = s.split("\n");

            for (int i = 0; i < temp.length; i++)
                currentURLs.add(temp[i]);

            currentImageIndex = 0;
            new GetImageAsync().execute(currentURLs.get(currentImageIndex));

            progressBar.setVisibility(View.INVISIBLE);
        }


        @Override
        protected void onProgressUpdate(Integer... values) {

            progressBar.setVisibility(View.VISIBLE);

        }


        @Override
        protected String doInBackground(String... strings) {


            publishProgress(0);


            HttpURLConnection connection = null;
            String result = null;

            try {
                String strUrl = "http://dev.theappsdr.com/apis/photos/index.php" + "?" +
                        "keyword=" + URLEncoder.encode(currentKeyword, "UTF-8");
                URL url = new URL(strUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = IOUtils.toString(connection.getInputStream(), "UTF8");
                }
            } catch (IOException e) {
                //Handle the exceptions
            } finally {
                //Close open connections and reader
            }
            return result;


        }
    }

    private class GetImageAsync extends AsyncTask<String, Void, Void> {
        //ImageView imageView;
        Bitmap bitmap = null;
        boolean noImage;



        @Override
        protected void onPostExecute(Void aVoid) {


                photoView.setImageBitmap(bitmap);
                progressBar.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
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
    }
}
