package com.example.test.hw04;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.util.ArrayList;

/*
* Homework 4
*
* Scott Schreiber
* Brianna Kirkpatrick
* Group5_HW04.zip
*
*
* */


public class MainActivity extends AppCompatActivity implements GetKeywordsAsync.KeywordIData, GetURLAsync.URLIData, GetImageAsync.ImageIData {


    CharSequence[] keywords;
    TextView photoName;
    ProgressBar progressBar;
    ImageView photoView;
    AlertDialog alert;
    int currentImageIndex;
    ArrayList<String> currentURLs;
    String currentKeyword;
    Button goButton;
    ImageView nextButton;
    ImageView prevButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //----------------------Elements-------------------------------//

        currentURLs = new ArrayList<>();
        currentImageIndex = 0;
        nextButton = (ImageView) findViewById(R.id.nextButton);
        prevButton = (ImageView) findViewById(R.id.prevButton);
        photoView = (ImageView) findViewById(R.id.photoView);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);

        photoName = (TextView) findViewById(R.id.photoNameView);
        goButton = (Button) findViewById(R.id.goButton);
        goButton.setEnabled(false);
        nextButton.setEnabled(false);
        prevButton.setEnabled(false);

        if(isConnected()) {
            progressBar.setVisibility(View.VISIBLE);
            new GetKeywordsAsync(MainActivity.this).execute("http://dev.theappsdr.com/apis/photos/keywords.php?format=json");
        }else{
            Toast.makeText(this, "No connection", Toast.LENGTH_SHORT).show();
        }



        //----------------------Listeners------------------------------//
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentImageIndex == currentURLs.size()-1)
                    currentImageIndex = -1;

                currentImageIndex +=1;
                new GetImageAsync(MainActivity.this).execute(currentURLs.get(currentImageIndex));
                Log.d("test",currentURLs.get(currentImageIndex));


            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(currentImageIndex == 0)
                    currentImageIndex = currentURLs.size();

                currentImageIndex -=1;
                new GetImageAsync(MainActivity.this).execute(currentURLs.get(currentImageIndex));

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

    @Override
    public void keywordPostExecute(ArrayList<String> s) {
        Log.d("test", "Retrieved keywords");

        keywords = new CharSequence[s.size()];

        for (int i = 0; i < keywords.length; i++){
            keywords[i] = s.get(i);

        }


        progressBar.setVisibility(View.INVISIBLE);
        goButton.setEnabled(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select Keyword")
                .setItems(keywords, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        photoName.setText(keywords[which]);
                        currentKeyword = photoName.getText().toString();
                        progressBar.setVisibility(View.VISIBLE);
                        new GetURLAsync(MainActivity.this, currentKeyword).execute();

                    }
                });
        alert = builder.create();
    }

    @Override
    public void urlPostExecute(ArrayList<String> s) {
        currentURLs.clear();

        for (int i = 0; i < s.size(); i++)
            currentURLs.add(s.get(i));

        currentImageIndex = 0;

        if(currentURLs.size() > 0) {
            new GetImageAsync(MainActivity.this).execute(currentURLs.get(currentImageIndex));
        }
        else{
            photoView.setImageResource(0);
            Toast.makeText(MainActivity.this, "No images found", Toast.LENGTH_SHORT).show();
        }

        if(currentURLs.size() == 1){
            nextButton.setEnabled(false);
            prevButton.setEnabled(false);

        }


        progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void imagePreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        nextButton.setEnabled(false);
        prevButton.setEnabled(false);
    }

    @Override
    public void imagePostExecute(Bitmap bitmap) {
        photoView.setImageBitmap(bitmap);
        progressBar.setVisibility(View.INVISIBLE);
        nextButton.setEnabled(true);
        prevButton.setEnabled(true);
    }
}