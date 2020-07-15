package com.example.bipotrack.database.CRUD;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import com.example.bipotrack.R;
import com.example.bipotrack.Utils;
import com.example.bipotrack.database.entites.DatabaseHelper;
import com.example.bipotrack.database.entites.Mood;
import com.example.bipotrack.database.entites.Note;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.util.Date;
import java.util.Vector;

public class SelectedMoodTable {

    private static final String TABLE_NAME = "selected_mood";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "DATE";
    private static final String COL_3 = "TIME";
    private static final String COL_4 = "MOOD";
    private static final String COL_5 = "NOTE";

    private static SQLiteDatabase db;
    private static SelectedMoodTable INSTANCE = null;


    private  SelectedMoodTable (SQLiteDatabase db){
        SelectedMoodTable.db = db;
        INSTANCE = this;
    }

    public static SelectedMoodTable getInstance(SQLiteDatabase db){
        if(INSTANCE == null)
            return new SelectedMoodTable ( db);
        else return INSTANCE;
    }
    public static SelectedMoodTable getInstance(){
        return INSTANCE;
    }


    public void createTable(){
        this.db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,DATE TEXT,TIME TEXT,MOOD TEXT,NOTE TEXT)");
    }
    public void dropTable(){
        this.db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }

    public long insertData(String mood, View view){

        Utils.DateTime dateTime = Utils.getCurrentTimeAndDate();
        boolean isInserted = false;
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,dateTime.getDate());
        contentValues.put(COL_3,dateTime.getTime());
        contentValues.put(COL_4,mood);
        Log.e("D", contentValues.toString());
        long result = this.db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            isInserted = false;
        else
            isInserted = true;

        if(isInserted == true) {
            Snackbar sb = Snackbar.make(view, "Your mood has been inserted successfully", Snackbar.LENGTH_SHORT);
            sb.getView().setBackgroundResource(R.color.success);
            sb.show();
        }
        else {
            Snackbar sb = Snackbar.make(view, "Your mood has not been inserted", Snackbar.LENGTH_SHORT);
            sb.getView().setBackgroundResource(R.color.danger);
            sb.show();
        }
        return result;
    }

    public long insertData(String mood, String note, View view){

        Utils.DateTime dateTime = Utils.getCurrentTimeAndDate();
        boolean isInserted = false;
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,dateTime.getDate());
        contentValues.put(COL_3,dateTime.getTime());
        contentValues.put(COL_4,mood);
        contentValues.put(COL_5,note);

        long result = this.db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            isInserted = false;
        else
            isInserted = true;

        if(isInserted == true) {
            Snackbar sb = Snackbar.make(view, "Your mood and notes has been inserted successfully", Snackbar.LENGTH_SHORT);
            sb.getView().setBackgroundResource(R.color.success);
            sb.show();
        }
        else {
            Snackbar sb = Snackbar.make(view, "Your mood and notes has not been inserted", Snackbar.LENGTH_SHORT);
            sb.getView().setBackgroundResource(R.color.danger);
            sb.show();
        }
        return result;
    }

    public Cursor getAllData() {
        Cursor res = this.db.rawQuery("select * from "+TABLE_NAME,null);

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append(COL_1 + " :"+ res.getString(0)+"\n");
            buffer.append(COL_2 + " :"+ res.getString(1)+"\n");
            buffer.append(COL_3 + " :"+ res.getString(2)+"\n");
            buffer.append(COL_4 + " :"+ res.getString(3)+"\n");
            buffer.append(COL_5 + " :"+ res.getString(4)+"\n\n");
        }
        Log.e("Data",buffer.toString());
        return res;
    }


    public Vector<Note> getMoodsIntervalDate(String start, String end) {
        Vector<Note> notes = new Vector<Note>();
        try {
            Cursor res = this.db.rawQuery("select * from "+ TABLE_NAME  ,null);

            Date startDate = java.text.DateFormat.getDateInstance().parse(start);
            Date endDate = java.text.DateFormat.getDateInstance().parse(end);
            while (res.moveToNext()) {

                String date = res.getString(1);
                Date theDate = java.text.DateFormat.getDateInstance().parse(date);

                if(startDate.compareTo(theDate) * theDate.compareTo(endDate)  >=  0) {
                    long id_note = res.getLong(0);
                    Note note = new Note(id_note,res.getString(1), res.getString(2),res.getString(3),res.getString(4) );
                    Vector<Mood> moods = NoteMoodsTable.getInstance().getMoodsByNote(id_note);
                    note.setMoodsOfNote(moods);
                    notes.add(note);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return notes;
    }


    public Vector<String> getDatesContainsMood() {
        Cursor res = this.db.rawQuery("select distinct " + COL_2 +" from "+ TABLE_NAME  ,null);

        Log.e("Clicked", "<<<<<<<<<<<<<<<" + res.getCount()) ;

        Vector<String> dates = new Vector<String>();
        while (res.moveToNext()) {
            String  date  = res.getString(0);
            Log.e("Clicked", "<<<<<<<<<<<<<<<" + date);
            dates.add(date);
        }
        return dates;
    }

    public long updateNoteById(Note noteObject, String note, View view) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,noteObject.id);
        contentValues.put(COL_2,noteObject.date);
        contentValues.put(COL_3,noteObject.time);
        contentValues.put(COL_4,noteObject.mood);
        contentValues.put(COL_5,note);
        int isUpdated = -1;
        isUpdated = db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { noteObject.id+"" });

        if(isUpdated != -1) {
            Snackbar sb = Snackbar.make(view, "Your mood and notes has been updated successfully", Snackbar.LENGTH_SHORT);
            sb.getView().setBackgroundResource(R.color.success);
            sb.show();
        }
        else {
            Snackbar sb = Snackbar.make(view, "Your mood and notes has not been updated", Snackbar.LENGTH_SHORT);
            sb.getView().setBackgroundResource(R.color.danger);
            sb.show();
        }

        return noteObject.id;
    }


    public Integer deleteData (Long id) {
        NoteMoodsTable.getInstance().deleteDataByNote(id);
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id+""});
    }

    public Vector<Note> getNotesByDate(String date) {
        Cursor res = this.db.rawQuery("select * from "+ TABLE_NAME + " where "+ COL_2 + " = \"" + date +"\"" ,null);
        Vector<Note> notes = new Vector<Note>();
        while (res.moveToNext()) {
            long id_note = res.getLong(0);
            Note note = new Note(id_note,res.getString(1), res.getString(2),res.getString(3),res.getString(4) );
            Vector<Mood> moods = NoteMoodsTable.getInstance().getMoodsByNote(id_note);
            note.setMoodsOfNote(moods);
            notes.add(note);
        }
        return notes;
    }

}
