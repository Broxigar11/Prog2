package com.example.myproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class InitializedKanjiStorage extends KanjiStorage{
    private int current_srs_stage;
    private int incorrect_adjustment_count;
    private LocalDateTime next_review_date;

    static final String[] SRStiming = new String[8];
    static{
        SRStiming[0] = "h4";
        SRStiming[1] = "h8";
        SRStiming[2] = "d1";
        SRStiming[3] = "d2";
        SRStiming[4] = "w1";
        SRStiming[5] = "w2";
        SRStiming[6] = "M1";
        SRStiming[7] = "M4";
    }

    public InitializedKanjiStorage(Parcel in)
    {
        //read in same order that you wrote in writeToParcel
        kanji = in.readString();
        meaning    = in.readString();
        reading = in.readString();
        mnemonic = in.readString();
        current_srs_stage = in.readInt();
        incorrect_adjustment_count = in.readInt();
        next_review_date = (LocalDateTime) in.readSerializable();
//      reading in a list custom objects: in.readTypedList(someCustomObjectArrayList, someCustomObject.CREATOR )

    }

    public InitializedKanjiStorage(KanjiStorage s){
        this.kanji = s.kanji;
        this.meaning = s.meaning;
        this.reading = s.reading;
        this.mnemonic = s.mnemonic;
        this.current_srs_stage = 0;
        this.incorrect_adjustment_count = 0;
        this.next_review_date = LocalDateTime.now();
    }

    public InitializedKanjiStorage(String k, String mean, String read, String mne, int srs, int ic, String date){
        this.kanji = k;
        this.meaning = mean;
        this.reading = read;
        this.mnemonic = mne;
        this.current_srs_stage = srs;
        this.incorrect_adjustment_count = ic;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        this.next_review_date = dateTime;
    }

    private void setNextReviewDate(){
        next_review_date=LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        if(current_srs_stage==0) return;

        switch (SRStiming[current_srs_stage-1].charAt(0)){
            case 's':
                next_review_date.plusSeconds(Long.parseLong(SRStiming[current_srs_stage-1].substring(1)));
                break;
            case 'm':
                next_review_date.plusMinutes(Long.parseLong(SRStiming[current_srs_stage-1].substring(1)));
                break;
            case 'h':
                next_review_date.plusHours(Long.parseLong(SRStiming[current_srs_stage-1].substring(1)));
                break;
            case 'd':
                next_review_date.plusDays(Long.parseLong(SRStiming[current_srs_stage-1].substring(1)));
                break;
            case 'w':
                next_review_date.plusWeeks(Long.parseLong(SRStiming[current_srs_stage-1].substring(1)));
                break;
            case 'M':
                next_review_date.plusMonths(Long.parseLong(SRStiming[current_srs_stage-1].substring(1)));
                break;
            default:
                break;
        }
    }

    public void CorrectAnswer(){
        current_srs_stage++;
        incorrect_adjustment_count=0;
        setNextReviewDate();
    }

    public void IncorrectAnswer(){
        int srs_penalty_factor;
        if(current_srs_stage>4) srs_penalty_factor=2;
        else srs_penalty_factor=1;

        incorrect_adjustment_count++;
        current_srs_stage = current_srs_stage - (incorrect_adjustment_count * srs_penalty_factor);

        if (current_srs_stage<1) current_srs_stage = 1;
        setNextReviewDate();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i)
    {
        //this is the order you should read in your contructor
        out.writeString(kanji);
        out.writeString(meaning);
        out.writeString(reading);
        out.writeString(mnemonic);
        out.writeInt(current_srs_stage);
        out.writeInt(incorrect_adjustment_count);
        out.writeSerializable(next_review_date);
//      writing some custom object: out.writeTypedList(someCustomObjectArrayList);

    }

    public static final Parcelable.Creator<InitializedKanjiStorage> CREATOR = new Parcelable.Creator<InitializedKanjiStorage>() {
        public InitializedKanjiStorage createFromParcel(Parcel in) {
            return new InitializedKanjiStorage(in);
        }
        public InitializedKanjiStorage[] newArray(int size) {
            return new InitializedKanjiStorage[size];
        }
    };

    public String getContentsAsString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String temp = kanji + ";" + meaning + ";" + reading + ";" + meaning + ";" + current_srs_stage + ";" + incorrect_adjustment_count + ";" + next_review_date.format(formatter) + "\n";
        return temp;
    }
    public LocalDateTime getNextReviewDate(){
        return next_review_date;
    }

}
