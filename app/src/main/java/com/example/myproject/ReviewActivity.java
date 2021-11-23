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


}