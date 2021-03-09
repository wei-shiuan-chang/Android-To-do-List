package com.hfad.todolistapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
//import android.support.v4.app.FragmentTransaction;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TodolistListFragment.Listener{
    //TodolistContentFragment details;
    long currentID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        TodolistListFragment details = new TodolistListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.topic_frag, details);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onStart(){
        super.onStart();

        currentID = (int) getIntent().getIntExtra("currentID",-1);
        if(currentID != -1){

            TodolistListFragment details = new TodolistListFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.topic_frag, details);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }

    }



    @Override
    public void itemClicked(long id) {
        View fragmentContainer = findViewById(R.id.fragment_container);
        currentID = id;
        if (fragmentContainer != null) {
            //Add the fragment to the FrameLayout
            TodolistContentFragment details = new TodolistContentFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            details.setTodolist(id);
            ft.replace(R.id.list_frag, details);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();


        } else {
            Log.v("id","Click "+id);
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_TODOLIST_ID, (int) id);
            startActivity(intent);
        }
    }
    public void addList(View view){


        EditText topicnameView = (EditText)findViewById(R.id.name);
        String topicname = topicnameView.getText().toString();


        if(!topicnameView.equals("")){

            SQLiteOpenHelper todolistDatabaseHelper = new TodolistDatabaseHelper(this);
            SQLiteDatabase db = todolistDatabaseHelper.getWritableDatabase();
            insertList(db,topicname, "","Todo", "");


            //Todolist.todolist[0].addList(listname);
            //Refresh the fragment to the FrameLayout
            TodolistListFragment details = new TodolistListFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //details.refreshList();
            /*
            ft.replace(R.id.list_frag, details);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();

             */
            //////Should be better this part
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("currentID", currentID);
            startActivity(intent);


        }

    }

    private static void insertList(SQLiteDatabase db, String topic, String item, String status, String description){
        ContentValues listValues = new ContentValues();
        listValues.put("ITEM", item);
        listValues.put("STATUS", status);
        listValues.put("DESCRIPTION", description);
        db.insert(topic, null, listValues);
    }
    public void findList(View view){
        EditText listnameView = (EditText)findViewById(R.id.name);
        String listname = listnameView.getText().toString();

        try{
            SQLiteOpenHelper todolistDatabaseHelper = new TodolistDatabaseHelper(this);
            SQLiteDatabase db = todolistDatabaseHelper.getReadableDatabase();

            Cursor cursor = db.query("LIST",
                    new String[]{"_id","NAME"},
                    null,null, null, null, "NAME COLLATE NOCASE ASC");
            Cursor cursorFind = db.query("LIST",
                    new String[]{"_id","NAME"},
                    "NAME = ?", new String[]{listname}, null, null, "NAME COLLATE NOCASE ASC");
            int counter = 1;
            Log.v("find", "cursor count: "+cursor.getCount()+"cursorFind count:"+cursorFind.getCount());
            cursorFind.moveToPosition(0);
            if(cursorFind.getCount() != 0){
                if (cursor.moveToFirst()) {
                    String nameText1 = cursor.getString(1);
                    String nameText2 = cursorFind.getString(1);
                    while (!nameText1.equals(nameText2)){
                        Log.v("find counter", nameText1+" !="+nameText2);
                        if(cursor.moveToNext()){
                            counter++;
                            nameText1 = cursor.getString(1);
                        }

                    }

                }
                Log.v("Counter", String.valueOf(counter));

                itemClicked(counter-1);
            }else{
                Log.v("toast", "fail to get the database");
                Toast toast = Toast.makeText(this, "The list is unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }



        }catch (SQLiteException e){
            Log.v("toast", "fail to get the database");
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();

        }



    }


    public void changeStatus(View view){
        //Todolist.todolist[todolistId].changeStatus();
        SQLiteOpenHelper todolistDatabaseHelper = new TodolistDatabaseHelper(this);
        SQLiteDatabase db = todolistDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("LIST",
                new String[]{"_id","NAME", "STATUS"},
                null, null, null, null, "NAME COLLATE NOCASE ASC");
        cursor.moveToPosition((int)currentID);
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
        TodolistContentFragment details = new TodolistContentFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        details.setTodolist(currentID);
        //details.refreshList();
        ft.replace(R.id.fragment_container, details);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();


    }

    public void removeList(View view){
        //Todolist.todolist[0].removeList(todolistId);
        SQLiteOpenHelper todolistDatabaseHelper = new TodolistDatabaseHelper(this);
        SQLiteDatabase db = todolistDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("LIST",
                new String[]{"_id","NAME", "STATUS"},
                null, null, null, null, "NAME COLLATE NOCASE ASC");
        cursor.moveToPosition((int)currentID);
        String nameText = cursor.getString(1);

        ContentValues data = new ContentValues();

        data.put("STATUS", "Todo");
        db.delete("LIST","NAME = ?", new String[]{nameText});


        TodolistListFragment details = new TodolistListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.list_frag, details);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();


    }



}