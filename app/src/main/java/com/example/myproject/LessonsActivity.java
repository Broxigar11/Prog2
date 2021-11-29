package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class LessonsActivity extends AppCompatActivity {
    ArrayList<KanjiStorage> list;

    TextView kanji;
    TextView reading;
    TextView meaning;
    AppCompatButton next;
    AppCompatButton prev;

    int current_kanji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        list = getIntent().getParcelableArrayListExtra("toLessons");
        current_kanji=0;

        kanji = findViewById(R.id.kanjiInquiry);
        reading = findViewById(R.id.reading);
        meaning = findViewById(R.id.meaning);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);

        setKanjiData(current_kanji);
    }

    public void onPrevLessonButtonClick(View view){
        if(current_kanji!=0) {
            current_kanji--;
            setKanjiData(current_kanji);
        }

    }

    public void onNextLessonButtonClick(View view){
        if(current_kanji < list.size()-1){
            current_kanji++;
            setKanjiData(current_kanji);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putParcelableArrayListExtra("fromLessons", list);
            startActivity(intent);
        }
    }

    public void setKanjiData(int i){
        KanjiStorage temp = list.get(i);
        kanji.setText(temp.kanji);
        reading.setText(temp.kanji);
        meaning.setText(temp.meaning);
        if(i==0) prev.setBackgroundColor(Color.parseColor("#616161"));
        else prev.setBackgroundColor(Color.parseColor("#4a7556"));
    }
}