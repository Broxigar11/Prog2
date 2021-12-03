package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private static ArrayList<InitializedKanjiStorage> kanji_list;
    ArrayList<KanjiStorage> lessons_list;
    ArrayList<InitializedKanjiStorage> reviews_list;

    int lessons_num;
    String db_path;
    String base_db_name;
    String initialized_db_name;

    AppCompatButton to_lessons;
    AppCompatButton to_reviews;
    TextView ok_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestPermissions(new String[] { Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1 );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        to_lessons = findViewById(R.id.toLessons);
        to_reviews = findViewById(R.id.toReviews);
        ok_text = findViewById(R.id.okText);

//        db_path = "/storage/emulated/0/Documents/";
//        base_db_name = db_path + "test.txt";
        db_path = this.getExternalFilesDir(null).toString();
        base_db_name = db_path + "/test.txt";
//        initialized_db_name= db_path + "/initialized_" + base_db_name;
        initialized_db_name= db_path + "/initialized_test.txt";

        lessons_list = null;
        reviews_list = null;
        lessons_list = getIntent().getParcelableArrayListExtra("fromLessons");
        reviews_list = getIntent().getParcelableArrayListExtra("fromReviews");

        lessons_num = 5;

        if(lessons_list != null) {
            reviews_list = new ArrayList<>();
            for (KanjiStorage k: lessons_list) {
                reviews_list.add(new InitializedKanjiStorage(k));
            }

            WriteToDB(reviews_list);
            deleteRows(lessons_num);

            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putParcelableArrayListExtra("toReviewsNew", reviews_list);
            startActivity(intent);

        } else if(reviews_list != null) {
            WriteToDB(reviews_list);
        }
        ReadFromBaseDB();
        ReadFromDB();



        ok_text.setVisibility(View.VISIBLE);
        File file = Environment.getExternalStorageDirectory();
        ok_text.setText(file.getAbsolutePath());







    }

    public void ReadFromDB(){
        if(kanji_list == null) kanji_list = new ArrayList<>();
        File file = new File(initialized_db_name);
        try {
            if(!file.exists()) file.createNewFile(); //TODO

            Scanner in = new Scanner(file);
            while(in.hasNextLine()) {
                String line = in.nextLine();
                String[] split = line.split(";");
                kanji_list.add(new InitializedKanjiStorage(split[0], split[1], split[2], split[3], Integer.parseInt(split[4]), Integer.parseInt(split[5]), split[6]));
//TODO load review_list
            }
            in.close();

        } catch (FileNotFoundException e) {
            ok_text.setVisibility(View.VISIBLE);
            ok_text.setText(e.toString());
        } catch (IOException e) {
            ok_text.setVisibility(View.VISIBLE);
            ok_text.setText(e.toString());
        }


    }

    public void ReadFromBaseDB(){
        lessons_list = new ArrayList<>();
        if(lessons_num > 0){
            File file = new File(base_db_name);
            try {

                Scanner in = new Scanner (file);
                for (int i = 0; i < lessons_num; i++) {
                    if(in.hasNextLine()) {
                        String line = in.nextLine();
                        String[] split = line.split(";");
                        lessons_list.add(new KanjiStorage(split[0], split[1], split[2], "")); //TODO
                    }
                }

                in.close();
            } catch (FileNotFoundException e) {
                ok_text.setVisibility(View.VISIBLE);
                ok_text.setText(e.toString());
            }
        }
    }

    public void WriteToDB(ArrayList<InitializedKanjiStorage> list){
        if(kanji_list==null) kanji_list = new ArrayList<>();
        kanji_list.addAll(list);
        DBorderByDate();
        try {
            File file = new File(initialized_db_name);
            PrintWriter out = new PrintWriter(file);
            for (InitializedKanjiStorage k : kanji_list) {
                out.write(k.getContentsAsString());
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deleteRows(int n){
        ArrayList<String> temp = new ArrayList<>();
        File file = new File(base_db_name);
        try {
            Scanner in = new Scanner(file);
            for(int i=0; i<n; i++) {
                if (in.hasNextLine()) in.nextLine();
            }
            while(in.hasNextLine()) {
                temp.add(in.nextLine() + "\n");
            }
            in.close();

            PrintWriter out = new PrintWriter(file);
            if(!temp.isEmpty()) {
                for (String s : temp) {
                    out.write(s);
                }
            }
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void DBorderByDate(){
        if(kanji_list != null) {
            for (int i = 0; i < kanji_list.size(); i++) {
                int temp_i = -1;
                for (int j = i + 1; j < kanji_list.size(); j++) {
                    if (kanji_list.get(j).getNextReviewDate().isBefore(kanji_list.get(i).getNextReviewDate())) {
                        temp_i = j;
                    }
                }
                if (temp_i != -1) Collections.swap(kanji_list, i, temp_i);
            }
        }
    }

    public void onLessonsButtonClick(View view){
        if(lessons_list != null && !lessons_list.isEmpty()){
            Intent intent = new Intent(this, LessonsActivity.class);
            intent.putParcelableArrayListExtra("toLessons", lessons_list);
            startActivity(intent);
        }
    }

    public void onReviewsButtonClick(View view){
        if(reviews_list != null && !reviews_list.isEmpty()){
                Intent intent = new Intent(this, ReviewActivity.class);
                intent.putParcelableArrayListExtra("toReviews", reviews_list);
                startActivity(intent);
        }
    }





}