package com.example.scott.baclevelcalculator;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    //---------------Global Variables------------------//
    double weight;
    boolean gender; //false = female, true = male
    final int stepSize = 5; //step interval for alcSlider
    double currentBACLevel = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //------------Components-----------------------------------------//
        final Switch genderSwitch = (Switch) findViewById(R.id.genderSwitch);
        final EditText weightInput = (EditText)findViewById(R.id.weightInput);
        final Button saveButton = (Button) findViewById(R.id.saveButton);
        final SeekBar alcSlider = (SeekBar)findViewById(R.id.alcSlider);
        final TextView alcPercentTrack = (TextView)findViewById(R.id.alcPercentTrack);
        final ProgressBar bacProgressBar = (ProgressBar)findViewById(R.id.bacProgressBar);
        final Button addDrinkButton = (Button) findViewById(R.id.addDrinkButton);
        final RadioGroup drinkRadioGroup = (RadioGroup)findViewById(R.id.drinkRadioGroup);
        final TextView bacLevelText = (TextView)findViewById(R.id.bacLevel);
        final TextView statusText = (TextView)findViewById(R.id.statusView);




        //---------Save Button-----------------------------------------//

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDouble(weightInput.getText().toString()) && Double.parseDouble(weightInput.getText().toString()) > 0.0) {
                    weight = Double.parseDouble(weightInput.getText().toString());
                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "Invalid input";
                    int duration = Toast.LENGTH_SHORT;

                    //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                gender = genderSwitch.isChecked();
            }
        });

        //---------Alcohol Slider------------------------------------//

        //alcSlider.setMax(25);
        alcSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                progress = ((int)Math.round(progress/stepSize))*stepSize;
                seekBar.setProgress(progress);
                alcPercentTrack.setText(progress+ "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //-------Add Drink--------------------------------//

        addDrinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentBACLevel < 0.25) {
                    int currentDrinkID = drinkRadioGroup.getCheckedRadioButtonId(); //gets the id of the currently selected drink size
                    double drinkSize = 0.0;
                    double drinkAlcPercent = ((double) alcSlider.getProgress()) / 100.0; //alcohol percentage
                    double bacLevel = 0.0;
                    double genderConst;
                    //sets the gender constant
                    if (gender) //male
                        genderConst = 0.68;
                    else //female
                        genderConst = 0.55;

                    switch (currentDrinkID) { //gets the double value of the drink size in oz
                        case R.id.oneOzRadio:
                            drinkSize = 1.0;
                            break;
                        case R.id.fiveOzRadio:
                            drinkSize = 5.0;
                            break;
                        case R.id.twelveOzRadio:
                            drinkSize = 12.0;
                            break;
                    }

                    bacLevel = ((drinkSize * drinkAlcPercent) * 6.24) / (weight * genderConst);
                    currentBACLevel += bacLevel;


                    bacProgressBar.setProgress((int) (currentBACLevel * 100));
                    bacLevelText.setText(Double.toString(currentBACLevel).substring(0, 4));
                }
                else{
                    Context context = getApplicationContext();
                    CharSequence text = "No more drinks for you.";
                    int duration = Toast.LENGTH_SHORT;

                    //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

                if(currentBACLevel <= 0.08){
                    statusText.setText("You're safe");
                }
                else if(currentBACLevel < 0.2){
                    statusText.setText("Be careful...");
                }
                else if(currentBACLevel <= 0.25){
                    statusText.setText("Over the limit!");

                }



            }
        });




    }

    protected boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
