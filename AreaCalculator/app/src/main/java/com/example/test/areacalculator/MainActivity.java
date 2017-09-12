/*
* In Class Assignment 2
* AreaCalculator
* Scott Schreiber
* Brad Rogers
*
*
* */


package com.example.test.areacalculator;

import android.content.Context;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //-------------------Elements-----------------------------------------------------//
        final TextView result = (TextView)findViewById(R.id.resultText);
        final TextView shape = (TextView)findViewById(R.id.shapeText);
        final TextView length1 = (TextView)findViewById(R.id.length1Text);
        final TextView length2 = (TextView)findViewById(R.id.length2Text);
        final EditText length1Field = (EditText) findViewById(R.id.length1InputField);
        final EditText length2Field = (EditText) findViewById(R.id.length2InputField);

        Log.d("test", "start");
        shape.setText("Select a shape");
        result.setVisibility(View.INVISIBLE);
        //better way to make text invisible?
        //-------------------Circle Button-------------------------------------------------//
        ImageButton circleButton = (ImageButton) findViewById(R.id.circleButton);
        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shape.setText("Circle");
                length2Field.setVisibility(View.INVISIBLE);
                length2.setVisibility(View.INVISIBLE);
                length2Field.setText("0");
            }
        });
        //-------------------Square Button-------------------------------------------------//
        ImageButton squareButton = (ImageButton) findViewById(R.id.squareButton);
        squareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shape.setText("Square");
                length2Field.setVisibility(View.INVISIBLE);
                length2.setVisibility(View.INVISIBLE);
                length2Field.setText("0");
            }
        });
        //-------------------Triangle Button-------------------------------------------------//
        ImageButton triangleButton = (ImageButton) findViewById(R.id.triangleButton);
        triangleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shape.setText("Triangle");
                length2Field.setVisibility(View.VISIBLE);
                length2.setVisibility(View.VISIBLE);
            }
        });
        //-------------------Calculate Button-------------------------------------------------//
        Button calculateButton = (Button) findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Double.parseDouble(length2Field.getText().toString()) > 0.0 && Double.parseDouble(length1Field.getText().toString()) > 0.0 && isDouble(length1Field.getText().toString()) && isDouble(length2Field.getText().toString())) {


                    if (shape.getText().toString().equals("Triangle")) {
                        double base = Double.parseDouble(length1Field.getText().toString());
                        double height = Double.parseDouble(length2Field.getText().toString());
                        result.setText(Double.toString(0.5 * base * height));
                        result.setVisibility(View.VISIBLE);
                    } else if (shape.getText().toString().equals("Circle")) {
                        double radius = Double.parseDouble(length1Field.getText().toString());
                        result.setText(Double.toString(Math.PI * Math.pow(radius, 2)));
                        result.setVisibility(View.VISIBLE);
                    } else if (shape.getText().toString().equals("Square")) {
                        double length = Double.parseDouble(length1Field.getText().toString());
                        result.setText(Double.toString(Math.pow(length, 2)));
                        result.setVisibility(View.VISIBLE);
                    }

                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "Invalid input";
                    int duration = Toast.LENGTH_SHORT;

                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
        //-------------------Clear Button-------------------------------------------------//
        Button clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                length1Field.setText("");
                length2Field.setText("");
                shape.setText("Select a shape");
                result.setVisibility(View.INVISIBLE);
                length2Field.setVisibility(View.VISIBLE);
                length2.setVisibility(View.VISIBLE);

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
