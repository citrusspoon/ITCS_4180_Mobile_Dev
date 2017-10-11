package com.example.test.hw03;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TriviaActivity extends AppCompatActivity {

    int currentQuestionIndex;
    int correctAnswers;
    TextView questionText;
    RadioGroup choicesRG;
    TextView loadStatus;
    Button nextButton;
    ProgressBar loadingBar;
    ImageView questionImage;
    TextView questionNumber;
    TextView timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        final ArrayList<Question> questionList = (ArrayList<Question>) getIntent().getExtras().getSerializable(MainActivity.QUESTION_LIST_KEY);


        correctAnswers = 0;

        Button quitButton = (Button) findViewById(R.id.quitButton);
        timer = (TextView) findViewById(R.id.timerView);
        nextButton = (Button) findViewById(R.id.nextButton);
        choicesRG = (RadioGroup) findViewById(R.id.choicesRadioGroup);
        loadingBar = (ProgressBar) findViewById(R.id.imageLoadingBar);
        questionImage = (ImageView) findViewById(R.id.questionImage);
        loadStatus = (TextView) findViewById(R.id.imageLoadStatus);
        questionText = (TextView) findViewById(R.id.questionView);
        questionNumber = (TextView) findViewById(R.id.questionNumberView);

        questionImage.setVisibility(View.INVISIBLE);
        loadingBar.setVisibility(View.VISIBLE);
        currentQuestionIndex = 0;



        loadQuestion(questionList.get(currentQuestionIndex));


        //---------------Listeners-------------------//

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check for correct answer first

                if(choicesRG.getCheckedRadioButtonId() == questionList.get(currentQuestionIndex).getAnswerIndex())
                    correctAnswers++;

                Log.d("test", "Submitted: " + Integer.toString(choicesRG.getCheckedRadioButtonId()));

                currentQuestionIndex++;
                if(currentQuestionIndex < questionList.size()) {
                    loadQuestion(questionList.get(currentQuestionIndex));
                }
                else{
                    //go to stats activity
                    Intent statsIntent = new Intent(TriviaActivity.this, StatsActivity.class);
                    Log.d("test", "Initiate Stats Activity");
                    //Log.d("test", "Correct Answers: " + correctAnswers + "/" + questionList.size());
                    statsIntent.putExtra(MainActivity.PERCENT_KEY, (double)correctAnswers/(double)questionList.size());
                    Log.d("test", "Sending double: " + (double)correctAnswers/(double)questionList.size());
                    statsIntent.putExtra(MainActivity.QUESTION_LIST_KEY, questionList);
                    finish();
                    startActivity(statsIntent);
                }


            }
        });

        choicesRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checked) {
                //Log.d("test", "Checked: " + checked);
            }
        });


        new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
               timer.setText("Time Left: " + millisUntilFinished / 1000 + " Seconds");

            }

            public void onFinish() {

                //timer.setText("Time's Up!");
                //go to stats activity
                Intent statsIntent = new Intent(TriviaActivity.this, StatsActivity.class);
                Log.d("test", "Initiate Stats Activity");
                //Log.d("test", "Correct Answers: " + correctAnswers + "/" + questionList.size());
                statsIntent.putExtra(MainActivity.PERCENT_KEY, (double)correctAnswers/(double)questionList.size());
                Log.d("test", "Sending double: " + (double)correctAnswers/(double)questionList.size());
                statsIntent.putExtra(MainActivity.QUESTION_LIST_KEY, questionList);
                finish();
                startActivity(statsIntent);
            }

        }.start();


    }

    private void loadQuestion(Question q){

        questionNumber.setText("Q" + (currentQuestionIndex+1));
        questionText.setText(q.getQuestion());
        choicesRG.removeAllViews();

        for(int i = 0; i < q.getChoices().size(); i++){

            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(q.getChoices().get(i));
            radioButton.setId(i);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            choicesRG.addView(radioButton, params);
        }

        choicesRG.clearCheck();
        //get image

        new GetImageAsync(questionImage, loadingBar, loadStatus, nextButton).execute(q.getImageURL());



    }
}
