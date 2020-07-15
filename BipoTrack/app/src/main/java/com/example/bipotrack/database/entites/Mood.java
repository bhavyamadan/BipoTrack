package com.example.bipotrack.database.entites;

public class Mood {
    public  Long id ;
    public  Long id_note ;
    public  String mood ;
    public  String confidence ;

    public Mood(Long id, Long id_note, String mood, String confidence) {
        this.id = id;
        this.id_note = id_note;
        this.mood = mood;
        this.confidence = confidence;
    }
}
