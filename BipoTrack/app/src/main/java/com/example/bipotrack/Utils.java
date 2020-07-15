package com.example.bipotrack;

import android.util.Log;
import android.widget.ImageView;

import java.util.Date;

public class Utils {

    public static class DateTime {
        private  String date;
        private  String time;
        public DateTime(String date, String time){
            this.date = date;
            this.time = time;
        }
        public String getDate() {
            return date;
        }
        public String getTime() {
            return time;
        }
    }
    public static DateTime getCurrentTimeAndDate(){
        String date = java.text.DateFormat.getDateInstance().format(new Date());
        String time = java.text.DateFormat.getTimeInstance().format(new Date());
        return new DateTime(date,time);
    }

    public static void setImageMoodIcon(ImageView iconMood, String mood){
        if(mood.equals("HAPPY"))
            iconMood.setImageResource(R.drawable.mood_happy);
        else if(mood.equals("GOOD"))
            iconMood.setImageResource(R.drawable.mood_good);
        else if(mood.equals("MEH"))
            iconMood.setImageResource(R.drawable.mood_meh);
        else if(mood.equals("ANGRY"))
            iconMood.setImageResource(R.drawable.mood_angry);
        else if(mood.equals("DEPRESSED"))
            iconMood.setImageResource(R.drawable.mood_depressed);
        else
            iconMood.setImageResource(R.drawable.mood_happy);
    }

    public static int getImageByMood(String mood) {
        if(mood.equals("HAPPY"))
            return R.drawable.mood_happy;
        else if(mood.equals("GOOD"))
            return R.drawable.mood_good;
        else if(mood.equals("MEH"))
            return R.drawable.mood_meh;
        else if(mood.equals("ANGRY"))
            return R.drawable.mood_angry;
        else if(mood.equals("DEPRESSED"))
            return R.drawable.mood_depressed;
        else
            return  R.drawable.mood_happy;
    }
}
