package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addTaskButton = MainActivity.this.findViewById(R.id.add_task_button);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("should be moving to add tasks activity");
                Intent goToAddTaskIntent = new Intent(MainActivity.this, AddTask.class);
                MainActivity.this.startActivity(goToAddTaskIntent);
            }
        });

        Button allTasksButton = MainActivity.this.findViewById(R.id.all_tasks_button);
        allTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAllTasksIntent = new Intent(MainActivity.this, AllTasks.class);
                MainActivity.this.startActivity(goToAllTasksIntent);
            }
        });

        Button taskDetailsButton = MainActivity.this.findViewById(R.id.detailsButton);
        taskDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToTaskDetailsIntent = new Intent(MainActivity.this, TaskDetail.class);
                MainActivity.this.startActivity(goToTaskDetailsIntent);
            }
        });

        Button settingsButton = MainActivity.this.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSettingsIntent = new Intent(MainActivity.this, Settings.class);
                MainActivity.this.startActivity(goToSettingsIntent);
            }
        });
    }
}