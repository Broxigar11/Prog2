package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //Storage[] kanjiList = new Storage [5];
    TextView kanjiInquiry;
    EditText answerInput;
    AppCompatButton NextButton;
    int currentInquiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /*
        kanjiList[0] = new Storage("一", "one", "いち");
        kanjiList[1] = new Storage("二", "two", "に");
        kanjiList[2] = new Storage("三", "three", "さん");
        kanjiList[3] = new Storage("四", "four", "し");
        kanjiList[4] = new Storage("五", "five", "ご");

        */

        kanjiInquiry = findViewById(R.id.kanjiInquiry);
        answerInput = findViewById(R.id.answerInput);
        NextButton = findViewById(R.id.NextButton);
        currentInquiry=0;
        //kanjiInquiry.setText(kanjiList[currentInquiry].kanji);



    }

    public void OnNextButtonClick (View view){
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