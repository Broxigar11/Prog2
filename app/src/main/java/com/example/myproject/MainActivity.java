package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private static ArrayList<InitializedKanjiStorage> kanji_list;
    ArrayList<KanjiStorage> lessons_list;
    ArrayList<InitializedKanjiStorage> reviews_list;

    int lessons_num;
    String base_db_name;
    String initialized_db_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        base_db_name = "test.txt";
        initialized_db_name= "initialized_" + base_db_name;

        lessons_list = getIntent().getParcelableArrayListExtra("fromLessons");
        reviews_list = getIntent().getParcelableArrayListExtra("fromReviews");

        /*if(lessonsList != null) {

            for (KanjiStorage k: toLessons) {
                toReviews.add(new InitializedKanjiStorage(k));
            }


        } else */

        lessons_num = 20;

        if(reviews_list != null) WriteToDB(reviews_list);
        ReadFromDB();
        ReadFromBaseDB();








    }

    public void ReadFromDB(){
        if(!kanji_list.isEmpty()){
            try {
                File file = new File(this.getFilesDir(), initialized_db_name);
                Scanner scanner = new Scanner (file);
                while(scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] split = line.split(";");
                    kanji_list.add(new InitializedKanjiStorage(split[0], split[1], split[2], split[3], Integer.parseInt(split[4]), Integer.parseInt(split[5]), split[6]));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void ReadFromBaseDB(){
        if(lessons_num > 0){
            try {
                File file = new File(this.getFilesDir(), base_db_name);
                Scanner scanner = new Scanner (file);
              for (int i = 0; i < lessons_num; i++) {
                    String line = scanner.nextLine();
                    String[] split = line.split(";");
                    lessons_list.add(new KanjiStorage(split[0], split[1], split[2], split[3]));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void WriteToDB(ArrayList<InitializedKanjiStorage> list){
        for(InitializedKanjiStorage l : list){
            kanji_list.add(l);
        }
        DBorderByDate();
    }

    public void DBorderByDate(){
        for (int i=0; i<kanji_list.size(); i++){
            int temp_i = -1;
            for(int j=i+1; j<kanji_list.size(); j++){
                if(kanji_list.get(j).getNextReviewDate().isBefore(kanji_list.get(i).getNextReviewDate())){
                    temp_i = j;
                }
            }
            if (temp_i!=-1) Collections.swap(kanji_list, i, temp_i);
        }
    }

    public void onLessonsButtonClick(View view){
        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putParcelableArrayListExtra("toLessons", lessons_list);
        startActivity(intent);
    }

    public void onReviewsButtonClick(View view){
        Intent intent = new Intent(this, LessonsActivity.class);
        intent.putParcelableArrayListExtra("toReviews", reviews_list);
        startActivity(intent);
    }





}