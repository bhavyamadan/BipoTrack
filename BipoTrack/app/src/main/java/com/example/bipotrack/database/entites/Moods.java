package com.example.bipotrack.database.entites;

public class Moods {

    public static int HAPPY = 500;
    public static int GOOD = 400;
    public static int MEH = 300;
    public static int ANGRY = 200;
    public static int DEPRESSED = 100;


    public static int getScoreFromMood(String moodTitle) {
        if(moodTitle.equals("HAPPY"))
            return HAPPY;
        if(moodTitle.equals("GOOD"))
            return GOOD;
        if(moodTitle.equals("MEH"))
            return MEH;
        if(moodTitle.equals("ANGRY"))
            return ANGRY;
        if(moodTitle.equals("DEPRESSED"))
            return DEPRESSED;
        else
            return -1;
    }
}
