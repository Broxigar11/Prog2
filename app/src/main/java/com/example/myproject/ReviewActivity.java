package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ReviewActivity extends AppCompatActivity {
    TextView kanjiInquiry;
    EditText answerInput;
    AppCompatButton NextButton;
    int currentInquiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        kanjiInquiry = findViewById(R.id.kanjiInquiry);
        answerInput = findViewById(R.id.answerInput);
        NextButton = findViewById(R.id.NextReviewButton);
        currentInquiry=0;

        //kanjiInquiry.setText(kanjiList[currentInquiry].kanji);
    }

    public void onNextReviewButtonClick (View view){
        /*
        if(answerInput.getText().toString().toLowerCase().equals(kanjiList[currentInquiry].meaning)){

            if(currentInquiry>3) currentInquiry=0;
            else currentInquiry++;

            kanjiInquiry.setText(kanjiList[currentInquiry].kanji);
            answerInput.setText("");
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "fuck", Toast.LENGTH_SHORT).show();
        }

         */
    }
}