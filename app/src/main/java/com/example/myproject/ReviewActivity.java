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
import java.util.Collections;

public class ReviewActivity extends AppCompatActivity {
    TextView kanjiInquiry;
    TextView question;
    EditText answerInput;
    AppCompatButton NextButton;

    ArrayList<InitializedKanjiStorage>[] kanji_list;
    int r_or_m;
    int[] current_kanji = {0,0};
    boolean isNew = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        kanjiInquiry = findViewById(R.id.kanjiInquiry);
        question = findViewById(R.id.question);
        answerInput = findViewById(R.id.answerInput);
        NextButton = findViewById(R.id.NextReviewButton);

        kanji_list[0] = getIntent().getParcelableArrayListExtra("toReviews");
        if(kanji_list[0]==null) {
            getIntent().getParcelableArrayListExtra("toReviewsNew");
            isNew = true;
        };
        kanji_list[1] = kanji_list[0];
        Collections.shuffle(kanji_list[0]);
        Collections.shuffle(kanji_list[1]);

    }

    public void onNextReviewButtonClick (View view){
        if(!isLast()){
            if (isCorrect())
                correct();
            else
                incorrect();

            setNextKanji();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putParcelableArrayListExtra("fromReviews", kanji_list[0]);
            startActivity(intent);
        }
    }

    public boolean isCorrect(){
        if(r_or_m == 0){
            return answerInput.getText().toString().equals(kanji_list[0].get(current_kanji[0]).getReading());
        }else{
            return answerInput.getText().toString().equals(kanji_list[1].get(current_kanji[1]).getMeaning());
        }
    }

    public boolean isLast(){
        return current_kanji[0] >= kanji_list[0].size()-1 && current_kanji[1] >= kanji_list[1].size()-1;
    }

    public void correct(){
        kanji_list[r_or_m].get(current_kanji[r_or_m]).correctAnswer(); //TODO calls twice because of shallow copy
        current_kanji[r_or_m]++;
        Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
    }

    public void incorrect(){
        if(isNew){
            InitializedKanjiStorage temp = kanji_list[r_or_m].get(current_kanji[r_or_m]);
            kanji_list[r_or_m].remove(current_kanji[r_or_m]);
            kanji_list[r_or_m].add(temp);
        }else{
            kanji_list[r_or_m].get(current_kanji[r_or_m]).incorrectAnswer(); //TODO calls twice because of shallow copy
        }
        Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
    }

    public void setNextKanji(){
        if(current_kanji[0] < kanji_list[0].size()-1 && current_kanji[1] < kanji_list[1].size()-1)
            r_or_m = (int) (Math.random() * 2);
        else if(current_kanji[1] >= kanji_list[1].size()-1)
            r_or_m = 0;
        else
            r_or_m = 1;

        if(r_or_m == 0)
            question.setText("Kanji reading:");
        else
            question.setText("Kanji meaning:");

        answerInput.setText("");
        kanjiInquiry.setText(kanji_list[r_or_m].get(current_kanji[r_or_m]).getKanji());
    }
}