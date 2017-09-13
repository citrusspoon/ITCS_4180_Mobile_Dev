package com.example.scott.baclevelcalculator;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //---------------Global Variables------------------//
    double weight;
    boolean gender; //false = female, true = male

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //------------Components-----------------------------------------//
        final Switch genderSwitch = (Switch) findViewById(R.id.genderSwitch);
        final EditText weightInput = (EditText)findViewById(R.id.weightInput);
        final Button saveButton = (Button) findViewById(R.id.saveButton);




        //---------Save Button-----------------------------------------//

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(isDouble(weightInput.getText().toString())) {
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
