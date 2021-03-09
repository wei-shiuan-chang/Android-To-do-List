package com.hfad.todolistapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import androidx.fragment.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.ListView;
import android.widget.Toast;

//import com.hfad.todolistapplication.Todolist;


public class TodolistTopicFragment extends ListFragment {

    ArrayAdapter<String> adapter;
    long currentID;

    static interface Listener {
        void itemClicked(long id);
        //void itemRefresh();
    };
    private Listener listener;
    LayoutInflater currentinflater;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("step", "onCreateView of List");
        currentinflater = inflater;

        String[] names = refreshList();

        adapter = new ArrayAdapter<>(
                inflater.getContext(), android.R.layout.simple_list_item_1, names);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    public String[] refreshList(){
        //Create a cursor

        SQLiteOpenHelper todolistDatabaseHelper = new TodolistDatabaseHelper(getActivity());
        try{
            SQLiteDatabase db = todolistDatabaseHelper.getReadableDatabase();

            Cursor cursor = db.query("LIST",
                    new String[]{"_id","NAME"},
                    null, null, null, null, "NAME COLLATE NOCASE ASC");

            int count = cursor.getCount();
            String[] names = new String[count];
            int counter = 0;
            //Log.v("step", "try cursor, count = "+count+", rawquery count = "+cursor2.getCount());

            if (cursor != null) {
                Log.v("step", "cursor != null");
                if (cursor.moveToFirst()) {
                    Log.v("step", "cursor.moveToFirst()");
                    do {
                        String nameText = cursor.getString(1);
                        Log.v("nameText", nameText);
                        names[counter] = nameText;
                        counter++;
                    } while (cursor.moveToNext());

                }
            }
            cursor.close();
            db.close();
            return names;


        }catch (SQLiteException e){
            Log.v("toast", "fail to get the database");
            Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
            return new String[]{"Fail to refresh"};
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (Listener)context; }
    @Override
    public void onListItemClick(ListView listView, View itemView, int position, long id) {
        if (listener != null) {
            listener.itemClicked(id);
            currentID = id;
        }
    }


}