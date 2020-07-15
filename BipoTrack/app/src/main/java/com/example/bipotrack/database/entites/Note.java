package com.example.bipotrack.database.entites;

import java.util.HashMap;
import java.util.Vector;


public class Note {
    public  Long id ;
    public  String date ;
    public  String time ;
    public  String mood ;
    public  String note ;
    public Vector<Mood> moodsOfNote = new Vector<>();

    public Note(long id, String date, String time, String mood, String note){
        this.id = id;
        this.date = date;
        this.time = time;
        this.mood = mood;
        this.note = note;
    }
    public Note (long id, String date, String time, String mood, String note, Vector<Mood> moodsOfNote){
        this.id = id;
        this.date = date;
        this.time = time;
        this.mood = mood;
        this.note = note;
        this.moodsOfNote = this.moodsOfNote ;
    }

    public void setMoodsOfNote(Vector<Mood> moodsOfNote){
        this.moodsOfNote = moodsOfNote;
    }
}