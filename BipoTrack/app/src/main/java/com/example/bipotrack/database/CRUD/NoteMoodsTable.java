package com.example.bipotrack.database.CRUD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bipotrack.R;
import com.example.bipotrack.Utils;
import com.example.bipotrack.database.entites.Mood;
import com.example.bipotrack.database.entites.Note;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class NoteMoodsTable {

    private static final String TABLE_NAME = "note_moods";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "ID_NOTE";
    private static final String COL_3 = "MOOD";
    private static final String COL_4 = "CONFIDENCE";

    private static SQLiteDatabase db;

    private static NoteMoodsTable INSTANCE = null;

    public NoteMoodsTable(SQLiteDatabase db){
        NoteMoodsTable.db = db;
        INSTANCE = this;
    }

    public static NoteMoodsTable getInstance(SQLiteDatabase db){
        if(INSTANCE == null)
            return new NoteMoodsTable (db);
        else return INSTANCE;
    }
    public static NoteMoodsTable getInstance(){
        return INSTANCE;
    }


    public void createTable(){
        this.db.execSQL("create table " + TABLE_NAME +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, ID_NOTE INTEGER NOT NULL, MOOD TEXT,CONFIDENCE TEXT, FOREIGN KEY (ID_NOTE) REFERENCES selected_mood(ID))");
    }
    public void dropTable(){
        this.db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }

    public Integer deleteDataByNote (Long id_note) {
        return db.delete(TABLE_NAME, "ID_NOTE = ?",new String[] {id_note+""});
    }
    public boolean insertData(String moodDetected, Float confidence, long id_note) {
        Utils.DateTime dateTime = Utils.getCurrentTimeAndDate();
        boolean isInserted = true;

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,id_note);
        contentValues.put(COL_3,moodDetected);
        contentValues.put(COL_4,confidence);

        long result = this.db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1) {
            isInserted = false;
        }
        else
            isInserted = true;

        return isInserted;
    }


    public Vector<Mood> getMoodsByNote(Long id_note) {
        Cursor res = this.db.rawQuery("select * from "+TABLE_NAME + " where "+ COL_2 + " = \"" + id_note +"\"" ,null);
        Vector<Mood> moods = new Vector<>();
        while (res.moveToNext()) {
            Mood mood = new Mood(res.getLong(0),id_note, res.getString(2),
                    res.getString(3) );
            moods.add(mood);
        }
        return moods;
    }

    public Cursor getAllData() {
        Cursor res = this.db.rawQuery("select * from "+TABLE_NAME,null);

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append(COL_1 + " :"+ res.getString(0)+"\n");
            buffer.append(COL_2 + " :"+ res.getString(1)+"\n");
            buffer.append(COL_3 + " :"+ res.getString(2)+"\n");
            buffer.append(COL_4 + " :"+ res.getString(3)+"\n\n");
        }
        Log.e("Data",buffer.toString());
        return res;
    }

}
