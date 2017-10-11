package com.example.test.hw03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Button quitButton = (Button) findViewById(R.id.statQuitButton);
        Button tryAgainButton = (Button) findViewById(R.id.tryAgainButton);
        ProgressBar percentBar = (ProgressBar) findViewById(R.id.percentBar);
        TextView percentLabel = (TextView) findViewById(R.id.percentLabel);
        final ArrayList<Question> questionList = (ArrayList<Question>) getIntent().getExtras().getSerializable(MainActivity.QUESTION_LIST_KEY);

        double percent = getIntent().getExtras().getDouble(MainActivity.PERCENT_KEY);
        percent *= 100;
        Log.d("test", "Recieved double: " + percent);

        percentLabel.setText((int) percent + "%");
        percentBar.setProgress((int)percent);




        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent triviaIntent = new Intent(StatsActivity.this, TriviaActivity.class);

                triviaIntent.putExtra(MainActivity.QUESTION_LIST_KEY, questionList);
                finish();
                startActivity(triviaIntent);
            }
        });


    }
}
