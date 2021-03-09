package com.hfad.todolistapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_TODOLIST_ID = "id";
    public int todolistId;
    TodolistContentFragment frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        frag = (TodolistContentFragment)
                getSupportFragmentManager().findFragmentById(R.id.detail_frag);

        todolistId = (int) getIntent().getExtras().get(EXTRA_TODOLIST_ID);
        frag.setTodolist(todolistId);

    }



    public void changeStatus(View view){
        //Todolist.todolist[todolistId].changeStatus();
        SQLiteOpenHelper todolistDatabaseHelper = new TodolistDatabaseHelper(this);
        SQLiteDatabase db = todolistDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("LIST",
                    new String[]{"_id","NAME", "STATUS"},
                    null, null, null, null, "NAME COLLATE NOCASE ASC");
        cursor.moveToPosition(todolistId);
        String nameText = cursor.getString(1);
        String statusText = cursor.getString(2);
        //Log.v("nameText, statusText", nameText +" "+ statusText);

        ContentValues data = new ContentValues();

        if(statusText.equals("Todo")){
            data.put("STATUS", "Doing");
            db.update("LIST", data,"NAME = ?", new String[]{nameText});
        }else if(statusText.equals("Doing")){
            data.put("STATUS", "Done");
            db.update("LIST", data,"NAME = ?", new String[]{nameText});
        }else{
            data.put("STATUS", "Todo");
            db.update("LIST", data,"NAME = ?", new String[]{nameText});
        }


        frag.setTodolist(todolistId);
        frag.onStart();


    }

    public void removeList(View view){
        //Todolist.todolist[0].removeList(todolistId);
        SQLiteOpenHelper todolistDatabaseHelper = new TodolistDatabaseHelper(this);
        SQLiteDatabase db = todolistDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("LIST",
                new String[]{"_id","NAME", "STATUS"},
                null, null, null, null, "NAME COLLATE NOCASE ASC");
        cursor.moveToPosition(todolistId);
        String nameText = cursor.getString(1);

        ContentValues data = new ContentValues();

        data.put("STATUS", "Todo");
        db.delete("LIST","NAME = ?", new String[]{nameText});

        todolistId--;
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("currentID", todolistId-1);
        startActivity(intent);


    }





}

