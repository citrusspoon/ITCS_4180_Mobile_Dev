package com.example.test.inclass03;

import android.app.Activity;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ImageView moodview = (ImageView)findViewById(R.id.moodImage);

        SeekBar selectMood = (SeekBar) findViewById(R.id.selectMood);
        selectMood.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int percentageChanged = 0;
            ImageView moodView = (ImageView) findViewById(R.id.moodImage);

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                percentageChanged = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                switch (percentageChanged){
                    case (0):
                        moodView.setImageResource(R.drawable.angry);
                        break;
                    case(1):
                        moodView.setImageResource(R.drawable.sad);
                        break;
                    case(2):
                        moodView.setImageResource(R.drawable.happy);
                        break;
                    case(3):
                        moodView.setImageResource(R.drawable.awesome);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        EditText nameInput = (EditText) findViewById(R.id.nameEditText);
        EditText emailInput = (EditText) findViewById(R.id.emailEditText);

        if (view.getId() == R.id.submitButton){
            if(nameInput.equals("")){
                CharSequence text = "Name input is missing!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(MainActivity.this, text, duration);
                toast.show();
            }
            if(emailInput.equals("")){
                CharSequence text = "Email format ";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(MainActivity.this, text, duration);
                toast.show();
            }

        }
    }
}
