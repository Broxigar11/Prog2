package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<InitializedKanjiStorage> KanjiList;
    ArrayList<KanjiStorage> toLessons;
    ArrayList<InitializedKanjiStorage> toReviews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toLessons = getIntent().getParcelableArrayListExtra("Lessons");
        toReviews = getIntent().getParcelableArrayListExtra("Reviews");

        if(toLessons != null) {
            for (KanjiStorage k: toLessons) {
                toReviews.add(new InitializedKanjiStorage(k));
            }
            Intent intent = new Intent(this, InitializedKanjiStorage.class);
            intent.putParcelableArrayListExtra("toReview", toReviews);
            startActivity(intent);
        } else if(toReviews != null) {

        } else {

        }




    }

    public void onPrevLessonButtonClick(View view){

    }

    public void onNextLessonButtonClick(View view){

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