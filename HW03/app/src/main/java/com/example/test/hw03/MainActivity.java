/**
 * HW 03
 *
 * Group5_HW03.zip
 *
 * Scott Schreiber
 * Brianna Kirkpatrick
 *
 *
 */




package com.example.test.hw03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    static final String QUESTION_LIST_KEY = "QUESTIONS";
    static final String PERCENT_KEY = "PERCENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<Question> questionList = new ArrayList<>();

        Button startButton = (Button) findViewById(R.id.startButton);
        Button exitButton = (Button) findViewById(R.id.exitButton);
        ProgressBar loadingBar = (ProgressBar) findViewById(R.id.imageLoadingBar);
        TextView loadStatus = (TextView) findViewById(R.id.loadStatus);
        ImageView triviaImage = (ImageView) findViewById(R.id.triviaImage);

        triviaImage.setVisibility(View.INVISIBLE);
        startButton.setEnabled(false);

        new GetTriviaAsync(questionList, startButton, loadingBar, loadStatus, triviaImage).execute();

        //----------------Listeners-----------------------//

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent triviaIntent = new Intent(MainActivity.this, TriviaActivity.class);

                triviaIntent.putExtra(QUESTION_LIST_KEY, questionList);
                startActivity(triviaIntent);
                //Log.d("test", questionList.get(0).getChoices().get(1));

            }
        });

    }



}
