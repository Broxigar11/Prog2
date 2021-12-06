package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private static ArrayList<InitializedKanjiStorage> kanji_list = null;
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

        lessons_num = 2;
        if(kanji_list==null) ReadFromDB();

        if(lessons_list != null) {
            reviews_list = new ArrayList<InitializedKanjiStorage>();
            for (KanjiStorage k: lessons_list) {
                reviews_list.add(new InitializedKanjiStorage(k));
            }

            editDB(reviews_list);
            deleteRows(lessons_num);

            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putParcelableArrayListExtra("toReviewsNew", reviews_list);
            startActivity(intent);

        } else if(reviews_list != null) {
            editDB(reviews_list);
            reviews_list = new ArrayList<InitializedKanjiStorage>();
        }

        ReadFromBaseDB();
        loadReviewList();

        if(lessons_list.isEmpty()) to_lessons.setBackgroundColor(Color.parseColor("#616161"));
        if(reviews_list.isEmpty()) to_reviews.setBackgroundColor(Color.parseColor("#616161"));

        ok_text.setVisibility(View.VISIBLE);






    }

    public void loadReviewList(){
        if(reviews_list == null) reviews_list = new ArrayList<InitializedKanjiStorage>();
        for (InitializedKanjiStorage k: kanji_list) {
            if(!k.getNextReviewDate().isAfter(LocalDateTime.now()))
                reviews_list.add(k);
            else
                break;
        }
    }

    public void ReadFromDB(){
        kanji_list = new ArrayList<>();
        File file = new File(initialized_db_name);
        try {
            if(!file.exists()) file.createNewFile();

            Scanner in = new Scanner(file);
            while(in.hasNextLine()) {
                String line = in.nextLine();
                String[] split = line.split(";");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(split[6], formatter);
                InitializedKanjiStorage temp = new InitializedKanjiStorage(split[0], split[1], split[2], split[3], Integer.parseInt(split[4]), Integer.parseInt(split[5]), dateTime);
                kanji_list.add(temp);
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
                        if(split.length<4) {
                            String[] temp = new String[4];
                            for(int j=0; j<4; j++){
                                if(j<split.length) temp[j]=split[j];
                                else temp[j]="";
                            }
                            split=temp;
                        };
                        lessons_list.add(new KanjiStorage(split[0], split[1], split[2], split[3]));
                    }
                }

                in.close();
            } catch (FileNotFoundException e) {
                ok_text.setVisibility(View.VISIBLE);
                ok_text.setText(e.toString());
            }
        }
    }

    public void editDB(ArrayList<InitializedKanjiStorage> list){
        //while(i<kanji_list.size() && list.size()>0){
        for(int i=0; i<kanji_list.size(); i++){
          //  if(list.size()>0){
                for (int j=0; j<list.size(); j++) {
                    if (kanji_list.get(i).getKanji().equals(list.get(j).getKanji())) {
                        kanji_list.set(i, new InitializedKanjiStorage(list.get(j)));
                        list.remove(j);
                        break;
                    }
                }
//            } else {
//                break;
//            }
        }
        if(!list.isEmpty()) kanji_list.addAll(list);
        writeDB();
    }

    public void writeDB(){
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