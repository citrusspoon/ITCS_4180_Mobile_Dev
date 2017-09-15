/*
* Homework 1
* HW1.zip
* Scott Schreiber
* */

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


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public class Drink {

        double size;
        double alcPercent;

        public Drink(double size, double alcPercent) {
            this.size = size;
            this.alcPercent = alcPercent;
        }

        public double getSize() {
            return size;
        }

        public double getAlcPercent() {
            return alcPercent;
        }
    }



    //---------------Global Variables------------------//
    double weight;
    boolean gender; //false = female, true = male
    final int stepSize = 5; //step interval for alcSlider
    double currentBACLevel = 0.0;
    ArrayList<Drink> drinkList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //------------Components-----------------------------------------//
        final EditText weightInput = (EditText)findViewById(R.id.weightInput);
        final TextView alcPercentTrack = (TextView)findViewById(R.id.alcPercentTrack);
        final TextView statusText = (TextView)findViewById(R.id.statusView);
        final TextView bacLevelText = (TextView)findViewById(R.id.bacLevel);

        final Button addDrinkButton = (Button) findViewById(R.id.addDrinkButton);
        final Button saveButton = (Button) findViewById(R.id.saveButton);
        final Button resetButton = (Button) findViewById(R.id.resetButton);
        final SeekBar alcSlider = (SeekBar)findViewById(R.id.alcSlider);
        final Switch genderSwitch = (Switch) findViewById(R.id.genderSwitch);
        final RadioGroup drinkRadioGroup = (RadioGroup)findViewById(R.id.drinkRadioGroup);
        final ProgressBar bacProgressBar = (ProgressBar)findViewById(R.id.bacProgressBar);


        //---------Save Button-----------------------------------------//

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDouble(weightInput.getText().toString()) && Double.parseDouble(weightInput.getText().toString()) > 0.0) {
                    weight = Double.parseDouble(weightInput.getText().toString());

                    Context context = getApplicationContext();
                    CharSequence text = "Information updated.";
                    int duration = Toast.LENGTH_SHORT;

                    //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else {
                    weightInput.setError("Enter a valid weight.");
                }
                gender = genderSwitch.isChecked();

                calculateBac(bacProgressBar, bacLevelText, statusText, addDrinkButton, saveButton);
            }
        });

        //-----------Reset Button-------------------------------------//
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove all drinks from the list
                drinkList.clear();
                //reset drink size radio button
                drinkRadioGroup.check(R.id.oneOzRadio);
                //reset alc % slider
                alcSlider.setProgress(5);
                //reset weight field
                weightInput.setText("");
                weight = 0.0;
                //reset gender switch
                genderSwitch.setChecked(false);
                //recalculates BAC
                calculateBac(bacProgressBar, bacLevelText, statusText, addDrinkButton, saveButton);
                // reenable buttons
                addDrinkButton.setEnabled(true);
                saveButton.setEnabled(true);
            }
        });


        //---------Alcohol Slider------------------------------------//


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

                if(weight > 0.0) {

                    if (currentBACLevel < 0.25) {

                        //get current drink size
                        int currentDrinkID = drinkRadioGroup.getCheckedRadioButtonId(); //gets the id of the currently selected drink size
                        double drinkSize = 0.0;
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
                        //get current drink %
                        double drinkAlcPercent = ((double) alcSlider.getProgress()) / 100.0; //alcohol percentage
                        //add new drink to list
                        drinkList.add(new Drink(drinkSize, drinkAlcPercent));
                        //calculate BAC
                        calculateBac(bacProgressBar, bacLevelText, statusText, addDrinkButton, saveButton);
                    } else {
                        Context context = getApplicationContext();
                        CharSequence text = "No more drinks for you.";
                        int duration = Toast.LENGTH_SHORT;

                        //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
                else{
                    Context context = getApplicationContext();
                    CharSequence text = "Enter weight.";
                    int duration = Toast.LENGTH_SHORT;

                    //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }
        });
    }

    protected void calculateBac(ProgressBar p, TextView t, TextView s, Button b1, Button b2){

        currentBACLevel = 0.0;
        p.setProgress(0);
        t.setText("0.00");

        if(drinkList.size() > 0) {


                double bacLevel = 0.0;
                double genderConst;
                //sets the gender constant
                if (gender) //male
                    genderConst = 0.68;
                else //female
                    genderConst = 0.55;


                for (Drink d : drinkList) {

                    bacLevel = ((d.getSize() * d.getAlcPercent()) * 6.24) / (weight * genderConst);
                    currentBACLevel += bacLevel;

                }
                p.setProgress((int) (currentBACLevel * 100));
                t.setText(Double.toString(currentBACLevel).substring(0, 4));

        }


        if(currentBACLevel <= 0.08){
            s.setText("You're safe");
            s.setBackgroundResource(R.color.green);
        }
        else if(currentBACLevel < 0.2){
            s.setText("Be careful...");
            s.setBackgroundResource(R.color.orange);
        }
        else{
            s.setText("Over the limit!");
            s.setBackgroundResource(R.color.red);
            //disable buttons
            if(currentBACLevel >= 0.25) {
                b1.setEnabled(false);
                b2.setEnabled(false);
            }

        }

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
