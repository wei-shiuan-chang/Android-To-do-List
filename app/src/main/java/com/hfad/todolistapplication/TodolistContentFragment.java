package com.hfad.todolistapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TodolistContentFragment extends Fragment {

    private long todolistId;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            todolistId = savedInstanceState.getLong("todolistId");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_todolist_content, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            String[] content = refreshContent((int) todolistId);

            TextView title = (TextView) view.findViewById(R.id.textTitle);
            //Todolist todolist = Todolist.todolist[(int) todolistId];
            //title.setText(todolist.getList());
            TextView description = (TextView) view.findViewById(R.id.textDescription);
            //description.setText(todolist.getContents());
            TextView statusView = (TextView)getView().findViewById(R.id.status);
            //statusView.setText(todolist.getStatus());
            title.setText(content[0]);
            description.setText(content[1]);
            statusView.setText(content[2]);

        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong("todolistId", todolistId);
    }

    public void setTodolist(long id){
        this.todolistId = id;

    }

    public String[] refreshContent(int id){
        //Create a cursor

        SQLiteOpenHelper todolistDatabaseHelper = new TodolistDatabaseHelper(getActivity());
        try{
            SQLiteDatabase db = todolistDatabaseHelper.getReadableDatabase();

            Cursor cursor = db.query("LIST",
                    new String[]{"_id","NAME", "STATUS", "DESCRIPTION"},
                    null, null, null, null, "NAME COLLATE NOCASE ASC");

            int count = cursor.getCount();
            String[] names = new String[count];
            int counter = 0;
            //Log.v("step", "try cursor, count = "+count+", rawquery count = "+cursor2.getCount());

            cursor.moveToPosition(id);
            String nameText = cursor.getString(1);
            String statusText = cursor.getString(2);
            String descriptionText = cursor.getString(3);

            cursor.close();
            db.close();
            return new String[]{nameText,statusText,descriptionText};


        }catch (SQLiteException e){
            Log.v("toast", "fail to get the database");
            Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
            return new String[]{"Fail to refresh"};
        }

    }


}