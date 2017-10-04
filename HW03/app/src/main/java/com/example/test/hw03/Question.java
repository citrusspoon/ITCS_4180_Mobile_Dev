package com.example.test.hw03;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by citru_000 on 10/3/2017.
 */

public class Question implements Serializable{

    private String question;
    private String imageURL;
    private ArrayList<String> choices;
    private int answerIndex;
    private int questionNumber;
    private String unSplitString;



    public Question(String unSplitString) {
        this.unSplitString = unSplitString;
        choices = new ArrayList<>();
        fillFields();
    }

    private void fillFields(){

        String[] temp = unSplitString.split(";");
        questionNumber = Integer.parseInt(temp[0]);
        question = temp[1];
        imageURL = temp[2];
        temp[temp.length-1]= temp[temp.length-1].replace(" ","");
        answerIndex = Integer.parseInt(temp[temp.length-1]);

        for(int i = 3; i < temp.length-1; i++)
            choices.add(temp[i]);


    }



    public String getUnSplitString() {
        return unSplitString;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public ArrayList<String> getChoices() {
        return choices;
    }



    public int getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex(int answerIndex) {
        this.answerIndex = answerIndex;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }
}
