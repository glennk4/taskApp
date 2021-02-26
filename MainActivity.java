package com.glendall.tasklist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseManager myDb ;
    Button addTaskBtn;
    ArrayList<Task> taskList = new ArrayList<Task>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb= new DatabaseManager(this);
        addTaskBtn = (Button) findViewById(R.id.addNewTaskBtn);
        addTaskBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, AddTask.class));
                    }
                }
        );

        viewAll();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        finish();
        startActivity(new Intent(String.valueOf(MainActivity.this)));
    }

    public void viewAll() {

        LinearLayout layout = (LinearLayout) findViewById(R.id.rootLayout);
        Cursor result = myDb.getAllData();

        if (result.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No Data to show", Toast.LENGTH_SHORT).show();
        } else {
            while (result.moveToNext()) {
                Task newTask = new Task();
                newTask.id = result.getInt(result.getColumnIndex("ID"));
                newTask.name = result.getString(result.getColumnIndex("NAME"));
                newTask.description = result.getString(result.getColumnIndex("DESCRIPTION"));
                newTask.dueDate = result.getString(result.getColumnIndex("DUE"));
                newTask.completed = Boolean.parseBoolean(result.getString(result.getColumnIndex("COMPLETED")));
                newTask.doneDate = result.getString(result.getColumnIndex("DONE_DATE"));
                taskList.add(newTask);
            }

            Button taskBtn[] = new Button[taskList.size()];
            for (int i = 0; i < taskList.size(); i++) {
                taskBtn[i] = new Button(this);
                layout.addView(taskBtn[i]);
                taskBtn[i].setText(taskList.get(i).getName());
                taskBtn[i].setId(i);
                taskBtn[i].setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int buttonId = v.getId();
                                System.out.println("button pressed is: "+buttonId);
                                System.out.println("test of id ="+taskList.get(buttonId).id);
                                viewTask(taskList,buttonId);
                            }
                        }
                );
            }
        }
    }

    public void viewTask(ArrayList<Task> taskList, int buttonId){

        int passedId = taskList.get(buttonId).getId();
        Intent intent = new Intent(getApplicationContext(),ViewTask.class);
        intent.putExtra("Task", passedId);
        startActivity(intent);
    }

}
