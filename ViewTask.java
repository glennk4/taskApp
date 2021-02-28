package com.glendall.tasklist;
/*
    TITLE: TASK LIST
    ACTIVITY: VIEW TASK
    AUTHOR: GLENN KENDALL
    DATE 26/02/2021
 */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewTask extends AppCompatActivity {

    DatabaseManager myDb ;
    int taskId = 0;
    Button completeBtn;
    Button deleteBtn;
    Button editBtn;
    Task selectedTask = new Task();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewtasklayout);
        Intent intent = getIntent();
        taskId = intent.getIntExtra("Task",0);

        myDb= new DatabaseManager(this);
        Cursor result = myDb.getTaskData(taskId);
        completeBtn = (Button)findViewById(R.id.completeBtn);
        completeBtn = (Button)findViewById(R.id.completeBtn);
        completeBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        completeTask(taskId);
                    }
                }
        );
        deleteBtn = (Button)findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteTask(taskId);
                    }
                }
        );

        selectedTask = new Task();
        EditText taskName = (EditText) findViewById(R.id.taskTitle);
        EditText taskDesc = (EditText) findViewById(R.id.descBox);
        EditText dueDate = (EditText) findViewById(R.id.taskDue);
        editBtn = (Button)findViewById(R.id.editBtn);
        editBtn = (Button)findViewById(R.id.editBtn);
        editBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editTask(taskId,selectedTask,taskName,taskDesc,dueDate);
                    }
                }
        );

        if (result.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No Data to show", Toast.LENGTH_SHORT).show();
        }
        else {
            while (result.moveToNext()) {
                selectedTask.id = result.getInt(result.getColumnIndex("ID"));
                selectedTask.name = result.getString(result.getColumnIndex("NAME"));
                selectedTask.description = result.getString(result.getColumnIndex("DESCRIPTION"));
                selectedTask.dueDate = result.getString(result.getColumnIndex("DUE"));
                selectedTask.completed = Boolean.parseBoolean(result.getString(result.getColumnIndex("COMPLETED")));
                selectedTask.doneDate = result.getString(result.getColumnIndex("DONE_DATE"));
            }
        }
        taskName.setText(selectedTask.name);
        taskDesc.setText(selectedTask.description);

//Format of date - needs fixing
        String newDate = selectedTask.dueDate;
        SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = sdf.parse(newDate);
            newDate =  date.toString();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        dueDate.setText(newDate);
       }


    public void deleteTask(int taskId){
        int deletedTask = taskId;
        myDb.deleteTask(deletedTask);
        Toast.makeText(ViewTask.this, "Task Deleted!",
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void completeTask(int taskId){
        int completedTask = taskId;
        myDb.completeTask(completedTask);
        Toast.makeText(ViewTask.this, "Task Completed!",
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void editTask(int taskId, Task selectedTask, EditText taskName, EditText taskDesc,
                         EditText dueDate){
        int editedTask = taskId;
        String name = taskName.getText().toString();
        String description = taskDesc.getText().toString();
        String due = dueDate.getText().toString();
        System.out.println("date: "+due);

        myDb.editTask(name,description,due,editedTask);
        Toast.makeText(ViewTask.this, "Task Edited!",
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}