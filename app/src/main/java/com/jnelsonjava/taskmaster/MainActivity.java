package com.jnelsonjava.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Laundry", "Do laundry", "assigned"));
        tasks.add(new Task("Dinner", "Make dinner", "assigned"));
        tasks.add(new Task("Hide and Seek", "Play hide and seek with the cat", "new"));
        tasks.add(new Task("Mulch", "Spread mulch out over the garden", "complete"));
        tasks.add(new Task("Laundry", "Do laundry", "assigned"));
        tasks.add(new Task("Dinner", "Make dinner", "assigned"));
        tasks.add(new Task("Hide and Seek", "Play hide and seek with the cat", "new"));
        tasks.add(new Task("Mulch", "Spread mulch out over the garden", "complete"));
        tasks.add(new Task("Laundry", "Do laundry", "assigned"));
        tasks.add(new Task("Dinner", "Make dinner", "assigned"));
        tasks.add(new Task("Hide and Seek", "Play hide and seek with the cat", "new"));
        tasks.add(new Task("Mulch", "Spread mulch out over the garden", "complete"));
        tasks.add(new Task("Laundry", "Do laundry", "assigned"));
        tasks.add(new Task("Dinner", "Make dinner", "assigned"));
        tasks.add(new Task("Hide and Seek", "Play hide and seek with the cat", "new"));
        tasks.add(new Task("Mulch", "Spread mulch out over the garden", "complete"));
        tasks.add(new Task("Laundry", "Do laundry", "assigned"));
        tasks.add(new Task("Dinner", "Make dinner", "assigned"));
        tasks.add(new Task("Hide and Seek", "Play hide and seek with the cat", "new"));
        tasks.add(new Task("Mulch", "Spread mulch out over the garden", "complete"));
        tasks.add(new Task("Laundry", "Do laundry", "assigned"));
        tasks.add(new Task("Dinner", "Make dinner", "assigned"));
        tasks.add(new Task("Hide and Seek", "Play hide and seek with the cat", "new"));
        tasks.add(new Task("Mulch", "Spread mulch out over the garden", "complete"));

        RecyclerView recyclerView = findViewById(R.id.task_list_main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TaskAdapter(tasks));

        // hard coded task buttons to be replaced by dynamic list
        Button task1Button = MainActivity.this.findViewById(R.id.taskButton1);
        Button task2Button = MainActivity.this.findViewById(R.id.taskButton2);
        Button task3Button = MainActivity.this.findViewById(R.id.taskButton3);
        task1Button.setOnClickListener(viewTaskDetail);
        task2Button.setOnClickListener(viewTaskDetail);
        task3Button.setOnClickListener(viewTaskDetail);

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

        Button settingsButton = MainActivity.this.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSettingsIntent = new Intent(MainActivity.this, Settings.class);
                MainActivity.this.startActivity(goToSettingsIntent);
            }
        });
    }

    @Override
    protected void  onResume() {

        super.onResume();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView taskListTitle = findViewById(R.id.taskListTitleTextView);
        String updatedText = preferences.getString("username", "") + " tasks";
        taskListTitle.setText(updatedText);
    }

    // reference for getting the text from a pressed button https://stackoverflow.com/questions/5620772/get-text-from-pressed-button
    // Function sends the clicked button's text in an intent starting the TaskDetail activity
    View.OnClickListener viewTaskDetail = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            Intent goToTaskDetailsIntent = new Intent(MainActivity.this, TaskDetail.class);
            goToTaskDetailsIntent.putExtra("task", button.getText().toString());
            MainActivity.this.startActivity(goToTaskDetailsIntent);
        }
    };
}