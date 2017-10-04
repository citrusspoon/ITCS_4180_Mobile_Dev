/*
* In Class 04
* Group5_InClass04.zip
* Scott Schreiber, Brianna Kirkpatrick
*
*
* */


package com.example.test.passwordgenerator;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Message;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //-------------Global variables--------------//

    ExecutorService threadPool;
    private Handler handler;


    int length = 8;
    int count = 1;
    ProgressBar taskProgress;
    TextView progressView;
    AlertDialog.Builder builder;
    TextView passView;

    ArrayList<String> passList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        threadPool = Executors.newFixedThreadPool(2);





        //----------------Progress dialog setup------------//

        taskProgress = (ProgressBar) findViewById(R.id.taskProgressBar);

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Passwords");


        //--------Set references to UI elements--------------//
        final TextView countView = (TextView) findViewById(R.id.countView);
        final TextView lengthView = (TextView) findViewById(R.id.lengthView);
         passView = (TextView) findViewById(R.id.passView);
        progressView = (TextView) findViewById(R.id.progressView);




        SeekBar lengthSeekBar = (SeekBar) findViewById(R.id.passLengthSeekBar);
        final SeekBar countSeekBar = (SeekBar) findViewById(R.id.passCountSeekBar);

        Button threadButton = (Button) findViewById(R.id.threadGenButton);
        Button asyncButton = (Button) findViewById(R.id.asyncGenButton);

        progressView.setVisibility(View.INVISIBLE);

        //---------------Button Listeners-------------------//

        threadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threadPool.execute(new CreatePasswords());
            }
        });

        asyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new genPassAsync().execute(count, length);
            }
        });

        //-------------Seekbar Listeners-------------------//

        lengthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                lengthView.setText((progress+8) + "");
                length = progress+8;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        countSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                countView.setText((progress+1) + "");
                count = progress+1;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {

                if(message.getData().containsKey("Password")){
                    ArrayList<String> passwords = message.getData().getStringArrayList("Password");
                    final String[] createdPasswords = new String[passwords.size()];
                    for(int i = 0; i < passwords.size(); i++){
                        //Log.d("handleMessage", i + " password is :" + passwords.get(i));
                        createdPasswords[i] = passwords.get(i);
                    }

                    builder.setItems(createdPasswords, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            passView.setText(createdPasswords[which]);
                            taskProgress.setProgress(0);
                            progressView.setVisibility(View.INVISIBLE);
                            passList.clear();
                        }
                    });

                    final AlertDialog alert = builder.create();

                    alert.show();

                }
                else if(message.getData().containsKey("Progress")){
                    Log.d("test", message.getData().getIntArray("Progress")[1] + "");
                    Log.d("test", message.getData().getIntArray("Progress")[0] + "");
                    taskProgress.setProgress(message.getData().getIntArray("Progress")[1]);
                    progressView.setText(message.getData().getIntArray("Progress")[0] + "/" + count);
                    progressView.setVisibility(View.VISIBLE);

                }
                return true;
            }
        });




    }

    class genPassAsync extends AsyncTask<Integer, Integer, String>{

        @Override
        protected void onPostExecute(String result) {

            final String[] items = new String[passList.size()];

            for(int j = 0; j < passList.size(); j++)
                items[j] = passList.get(j);


            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    passView.setText(items[which]);
                    taskProgress.setProgress(0);
                    progressView.setVisibility(View.INVISIBLE);
                    passList.clear();
                }
            });

            final AlertDialog alert = builder.create();

            alert.show();


        }

        @Override
        protected void onProgressUpdate(Integer... integers) {

            //integers[0] is number generated so far [1] is total to be generated

            double p = ((double)integers[0]/(double)integers[1])*100.0;
            taskProgress.setProgress((int)p);
            progressView.setText(integers[0] + "/" + integers[1]);
            progressView.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(Integer... integers) {

            //0 is count, 1 is length
            Log.d("test" , "async started");
            publishProgress(0, integers[0]);
            for(int i = 1; i <= integers[0]; i++){
                passList.add(Util.getPassword(integers[1]));
                publishProgress(i, integers[0]);
            }


            Log.d("test" , "async ended");

            return null;
        }
    }


    public class CreatePasswords implements Runnable{
        ArrayList<String> list= new ArrayList<>();
        @Override
        public void run() {

            int[] progressArray = new int[2];
            //Create passwords based on how many we want.
            for(int i = 0; i< count; i++){
                progressArray[0] = i;
                double p = ((double)i/(double)count)*100.0;
                progressArray[1] = (int)p;
                sendMsg(progressArray);
                Log.d("test", "Thread started");
                list.add(Util.getPassword(length)); //returns a string.
                Log.d("cw4", "Password is :" + list.get(i));






            }
            Log.d("test", "Thread ended");
            progressArray[0] = count;
            progressArray[1] = 100;
            sendMsg(progressArray);
            sendMsg(list);
        }
        private void sendMsg(ArrayList<String> msgText){
            Bundle bundle = new Bundle();
            //String key is "Password"
            bundle.putStringArrayList("Password", msgText);
            Message message = new Message();
            message.setData(bundle);
            handler.sendMessage(message);
        }

        private void sendMsg(int[] p){
            Bundle bundle2 = new Bundle();
            bundle2.putIntArray("Progress", p);
            Message message = new Message();
            message.setData(bundle2);
            handler.sendMessage(message);

        }
    }
}
