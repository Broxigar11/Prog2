package com.example.myproject;

import android.os.Parcel;
import android.os.Parcelable;

public class KanjiStorage implements Parcelable{
    protected String kanji;
    protected String meaning;
    protected String reading;
    protected String mnemonic;

    public KanjiStorage() {
        this.kanji = "";
        this.meaning = "";
        this.reading = "";
        this.mnemonic = "";
    }

    public KanjiStorage(String k, String mean, String r, String mne) {
        this.kanji = k;
        this.meaning = mean;
        this.reading = r;
        this.mnemonic = mne;
    }

    public KanjiStorage(Parcel in)
    {
        //read in same order that you wrote in writeToParcel
        kanji = in.readString();
        meaning    = in.readString();
        reading = in.readString();
        mnemonic = in.readString();
//      reading in a list custom objects: in.readTypedList(someCustomObjectArrayList, someCustomObject.CREATOR )

    }

    public String getKanji(){
        return kanji;
    }
    public String getMeaning(){
        return meaning;
    }
    public String getReading(){
        return reading;
    }
    public String getMnemonic(){
        return mnemonic;
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
//      writing some custom object: out.writeTypedList(someCustomObjectArrayList);

    }

    public static final Parcelable.Creator<KanjiStorage> CREATOR = new Parcelable.Creator<KanjiStorage>() {
        public KanjiStorage createFromParcel(Parcel in) {
            return new KanjiStorage(in);
        }
        public KanjiStorage[] newArray(int size) {
            return new KanjiStorage[size];
        }
    };
}
