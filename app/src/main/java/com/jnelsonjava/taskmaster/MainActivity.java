package com.jnelsonjava.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskInstance;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.TaskListenable {

    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // initialize amplify
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());

            Log.i("MainActivityAmplify", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MainActivityAmplify", "Could not initialize Amplify", error);
        }








        db = Room.databaseBuilder(getApplicationContext(), Database.class, "jnelson_task_master")
                .allowMainThreadQueries()
                .build();

        List<Task> tasks = db.taskDAO().getTasksSortByRecent();

        RecyclerView recyclerView = findViewById(R.id.task_list_main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TaskAdapter(tasks, this));


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

        db = Room.databaseBuilder(getApplicationContext(), Database.class, "jnelson_task_master")
                .allowMainThreadQueries()
                .build();
        List<Task> tasks = db.taskDAO().getTasksSortByRecent();

        RecyclerView recyclerView = findViewById(R.id.task_list_main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TaskAdapter(tasks, this));
    }

    @Override
    public void taskListener(Task task) {
        Intent goToTaskDetailsIntent = new Intent(MainActivity.this, TaskDetail.class);
        goToTaskDetailsIntent.putExtra("title", task.title);
        goToTaskDetailsIntent.putExtra("body", task.body);
        goToTaskDetailsIntent.putExtra("state", task.state);
        this.startActivity(goToTaskDetailsIntent);
    }
}