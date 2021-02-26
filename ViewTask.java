package com.glendall.tasklist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewTask extends AppCompatActivity {

    DatabaseManager myDb ;
    int taskId = 0;
    Button completeBtn;
    Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewtasklayout);
        Intent intent = getIntent();
        taskId = intent.getIntExtra("Task",0);

        myDb= new DatabaseManager(this);
        Cursor result = myDb.getTaskData(taskId);
        completeBtn = (Button)findViewById(R.id.completeBtn);
        deleteBtn = (Button)findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteTask(taskId);
                    }
                }
        );

        Task selectedTask = new Task();
        TextView taskName = (TextView) findViewById(R.id.taskTitle);
        TextView taskDesc = (TextView) findViewById(R.id.descBox);
        TextView dueDate = (TextView) findViewById(R.id.taskDue);

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
        dueDate.setText(selectedTask.dueDate.toString());

        System.out.println("TEST OF BOOL: "+selectedTask.completed);
    }

    public void deleteTask(int taskId){
        int deletedTask = taskId;
        myDb.deleteTask(deletedTask);
        Toast.makeText(ViewTask.this, "Task Deleted!",
                Toast.LENGTH_LONG).show();

    }
}