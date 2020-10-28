package com.jnelsonjava.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.ApiOperation;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.api.graphql.model.ModelSubscription;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskInstance;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.TaskListenable {

    Database db;
    RecyclerView recyclerView;

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

        List<TaskInstance> taskInstances = new ArrayList<>();


        Handler handler = new Handler(Looper.getMainLooper(),
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        recyclerView.getAdapter().notifyDataSetChanged();
                        return false;
                    }
                });

        Handler handleItemAdded = new Handler(Looper.getMainLooper(),
                message -> {
                    recyclerView.getAdapter().notifyItemInserted(taskInstances.size() - 1);
                    Toast.makeText(this, "New Task!", Toast.LENGTH_SHORT).show();
                    return false;
                });



//        db = Room.databaseBuilder(getApplicationContext(), Database.class, "jnelson_task_master")
//                .allowMainThreadQueries()
//                .build();
//
//        List<TaskInstance> tasks = db.taskInstanceDAO().getTasksSortByRecent();


        Amplify.API.query(
                ModelQuery.list(TaskInstance.class),
                response -> {
                    for (TaskInstance taskInstance : response.getData()) {
                        taskInstances.add(taskInstance);
                    }
                    handler.sendEmptyMessage(1);
                    Log.i("Amplify.queryitems", "Got this many items from dynamo " + taskInstances.size());


                },
                error -> Log.i("Amplify.queryitems", "Did not get items"));



        recyclerView = findViewById(R.id.task_list_main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TaskAdapter(taskInstances, this));



        ApiOperation subscription = Amplify.API.subscribe(
                ModelSubscription.onCreate(TaskInstance.class),
                onEstablished -> Log.i("Amplify.Subscription", "Subscription established"),
                onCreated -> {
                    Log.i("Amplify.Subscription", "Todo create subscription received: " + ((TaskInstance) onCreated.getData()).getTitle());
                    TaskInstance newTask = (TaskInstance) onCreated.getData();
                    taskInstances.add(newTask);
                    recyclerView.getAdapter().notifyItemInserted(1);
                },
                onFailure -> Log.e("Amplify.Subscription", "Subscription failed", onFailure),
                () -> Log.i("Amplify.Subscription", "Subscription completed")
        );


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

//        db = Room.databaseBuilder(getApplicationContext(), Database.class, "jnelson_task_master")
//                .allowMainThreadQueries()
//                .build();
//        List<TaskInstance> tasks = db.taskInstanceDAO().getTasksSortByRecent();



        RecyclerView recyclerView = findViewById(R.id.task_list_main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new TaskAdapter(tasks, this));
    }

    @Override
    public void taskListener(TaskInstance task) {
        Intent goToTaskDetailsIntent = new Intent(MainActivity.this, TaskDetail.class);
        goToTaskDetailsIntent.putExtra("title", task.getTitle());
        goToTaskDetailsIntent.putExtra("body", task.getBody());
        goToTaskDetailsIntent.putExtra("state", task.getState());
        this.startActivity(goToTaskDetailsIntent);
    }
}