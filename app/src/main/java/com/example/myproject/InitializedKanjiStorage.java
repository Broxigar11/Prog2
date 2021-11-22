package com.example.myproject;

import java.time.LocalDateTime;
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

    public InitializedKanjiStorage(KanjiStorage s){
        this.kanji = s.kanji;
        this.meaning = s.meaning;
        this.kunyomi = s.kunyomi;
        this.onyomi = s.onyomi;
        this.mnemonic = s.mnemonic;
        this.current_srs_stage = 0;
        this.incorrect_adjustment_count = 0;
        this.next_review_date = next_review_date.now();
    }

    private void setNextReviewDate(){
        next_review_date=next_review_date.now().truncatedTo(ChronoUnit.HOURS);
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

    public void SortByNextReviewDate(){

    }



}
