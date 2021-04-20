package com.example.st_task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Adapter.ItemPress {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter adapter;

    FloatingActionButton add;

    DBHelper DB;

    String name, details, exists, status;
    int id=0;
    int det;

    ArrayList<Tasks> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize DB Helper to connect main activity to SQLite
        DB = new DBHelper(MainActivity.this, "Tasks", new SQLiteDatabase.CursorFactory() {
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
                return null;
            }
        }, 1);

        add = findViewById(R.id.fbtnAdd);
        mRecyclerView = findViewById(R.id.recycTask);

        //get population count of tasks in DB
        getCount();
        //populate the recyclerview to show tasks user added
        showTasks();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTask();
            }
        });

    }

    public void AddTask(){

        //create a popup to get task and task details
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_task, null);

        final EditText editText = (EditText) dialogView.findViewById(R.id.txtTitle);
        final EditText editText1 = (EditText) dialogView.findViewById(R.id.txtDetails);
        Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
        Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = editText.getText().toString();
                details = editText1.getText().toString();

                id = id+1;
                saveTasks();

                //Toast.makeText(MainActivity.this, String.valueOf(id), Toast.LENGTH_SHORT).show();

                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }

    public void saveTasks(){
        boolean result = DB.saveTask(String.valueOf(id), name, details, "Incomplete"); //add and check whether task is added to the DB
        if (result == true) {
            Toast.makeText(MainActivity.this, "SUCCESSFULLY ADDED!", Toast.LENGTH_SHORT).show();
            showTasks();//update recyclerview
            getCount();//update task count
        } else {
            Toast.makeText(MainActivity.this, "ERROR ADDING!", Toast.LENGTH_SHORT).show();
        }

    }

    private void showDetails(){

        //get task details
        Cursor cursor = DB.getTask(String.valueOf(det));

        if(cursor != null && cursor.moveToFirst()){

            //create popup and show details
            final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.add_task, null);

            final EditText editText = (EditText) dialogView.findViewById(R.id.txtTitle);
            final EditText editText1 = (EditText) dialogView.findViewById(R.id.txtDetails);
            final TextView tv = (TextView) dialogView.findViewById(R.id.textView);
            tv.setText("DETAILS");
            Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);

            if (cursor.getString(3).equals("Complete")) {
                button1.setText("INCOMPLETE");
                status = "Incomplete";
            }else{
                status = "Complete";
                button1.setText("COMPLETE");
            }
            Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);

            editText.setEnabled(false);
            editText1.setEnabled(false);

           editText.setText(cursor.getString(1));
           editText1.setText(cursor.getString(2));

            name =(cursor.getString(1));
            details=(cursor.getString(2));

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder.dismiss();
                }
            });
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteTask();//delete completed task
                    dialogBuilder.dismiss();
                }
            });

            dialogBuilder.setView(dialogView);
            dialogBuilder.show();

            cursor.close();
        }
    }

    public void showTasks(){

        //load tasks into array from DB
        items = new ArrayList<>();
        Cursor res = DB.getAllTasks();
        if (res.getCount() == 0){

        }else {
            while (res.moveToNext()) {
                items.add(new Tasks(res.getString(0), res.getString(1), res.getString(3)));
            }
        }

        //push array values to show in recyclerview
        adapter = new Adapter(items, this);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this.getApplicationContext()));
    }

    public int getCount(){
        //get size of DB to get pk of next task user puts
        id = DB.getProfilesCount();
        return id;
    }

    private void deleteTask(){

        //delete completed task from the DB

        boolean result = DB.updateTask(String.valueOf(det), name, details, status);
        if (result==true){
            Toast.makeText(this, status.toUpperCase(), Toast.LENGTH_SHORT).show();
            getCount();//update tasks count
            showTasks();//update recyclerview
        }else{
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void Click(int position) {
        //recyclerview on click function
        det = Integer.parseInt(items.get(position).getId());
        showDetails();
    }
}