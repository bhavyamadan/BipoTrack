package com.example.bipotrack.database.entites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.bipotrack.database.CRUD.NoteMoodsTable;
import com.example.bipotrack.database.CRUD.SelectedMoodTable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bipotrack.db";
    public static Context context;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        DatabaseHelper.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("Database", "onCreate Database");
        try {
            SelectedMoodTable.getInstance(db).createTable();
        }catch (Exception e){
            Log.e("Warn","SelectedMoodTable Exist ");
        }
        try {
            new NoteMoodsTable(db).createTable();
        }catch (Exception e){
            Log.e("Warn","NoteMoodsTable Exist");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("Database", "onUpgrade");
        SelectedMoodTable.getInstance(db).dropTable();
        new NoteMoodsTable(db).dropTable();
        onCreate(db);
    }
}
