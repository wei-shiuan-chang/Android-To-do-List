package com.hfad.todolistapplication;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;


public class TodolistDatabaseHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "todolist"; //the name of the database
    private static final int DB_VERSION = 1; //the version of the database

    TodolistDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("Database","Oncreate");
        updateMyDatabase(db, 0, DB_VERSION);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion){

        if(oldVersion < 1){
            /*
            db.execSQL("CREATE TABLE LIST (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "TOPIC TEXT," +
                    "ITEM TEXT," +
                    "STATUS TEXT," +
                    "DESCRIPTION TEXT);");

             */

            //Don't insert to the table
            //insertList(db,"Black Friday", "Doing", "Tommy Hilfiger");
            //insertList(db,"Call Verna", "Undo", "Phone number: 4648373");
            Log.v("Database","Original");
        }

    }

    private static void insertList(SQLiteDatabase db, String topic, String item, String status, String description){
        ContentValues listValues = new ContentValues();
        listValues.put("ITEM", item);
        listValues.put("STATUS", status);
        listValues.put("DESCRIPTION", description);
        db.insert(topic, null, listValues);
    }

}
