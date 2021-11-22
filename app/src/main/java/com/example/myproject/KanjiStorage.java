package com.example.myproject;

public class KanjiStorage {
    protected String kanji;
    protected String meaning;
    protected String kunyomi;
    protected String onyomi;
    protected String mnemonic;

    public KanjiStorage() {
        this.kanji = "";
        this.meaning = "";
        this.kunyomi = "";
        this.onyomi = "";
        this.mnemonic = "";
    }

    public KanjiStorage(String k, String mean, String kun, String on, String mne) {
        this.kanji = k;
        this.meaning = mean;
        this.kunyomi = kun;
        this.onyomi = on;
        this.mnemonic = mne;
    }

    public String getKanji(){
        return kanji;
    }
    public String getMeaning(){
        return meaning;
    }
    public String getKunyomi(){
        return kunyomi;
    }
    public String getOnyomi(){
        return onyomi;
    }
    public String getMnemonic(){
        return mnemonic;
    }
}
