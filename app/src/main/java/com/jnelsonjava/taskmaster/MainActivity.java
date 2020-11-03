package com.jnelsonjava.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.TaskListenable {

//    Database db;
    RecyclerView recyclerView;
    List<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize amplify
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());

            Log.i("MainActivityAmplify", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MainActivityAmplify", "Could not initialize Amplify", error);
        }

        Handler handleCheckLoggedIn = new Handler(Looper.getMainLooper(), message -> {
            if (message.arg1 == 0) {
                Log.i("Amplify.login", "handler: they were not logged in");
            } else if (message.arg1 == 1){
                Log.i("Amplify.login", "handler: they were logged in!");
            } else {
                Log.i("Amplify.login", "handler: ?????????");
            }
            return false;
        });

        Amplify.Auth.fetchAuthSession(
                result -> {
                    Log.i("AmplifyQuickstart", result.toString());
                    Message message = new Message();
                    if (result.isSignedIn()) {
                        message.arg1 = 1;
                    } else {
                        message.arg1 = 0;
                    }
                    handleCheckLoggedIn.sendMessage(message);
                },
                error -> Log.e("AmplifyQuickstart", error.toString())
        );

//        addTestUser();
//        verifyTestUser();
//        signInTestUser();


        tasks = new ArrayList<>();


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
                    recyclerView.getAdapter().notifyItemInserted(tasks.size() - 1);
                    Toast.makeText(this, "New Task!", Toast.LENGTH_SHORT).show();
                    return false;
                });





//        db = Room.databaseBuilder(getApplicationContext(), Database.class, "jnelson_task_master")
//                .allowMainThreadQueries()
//                .build();
//
//        List<TaskInstance> tasks = db.taskInstanceDAO().getTasksSortByRecent();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);


        Amplify.API.query(
                ModelQuery.list(Task.class),
                response -> {
                    for (Task task : response.getData()) {
                        if (task.getTeam().getName().equals(preferences.getString("team", null))) {
                            tasks.add(task);
                            Log.i("a task", task.toString());
                        }
                    }
                    handler.sendEmptyMessage(1);
                    Log.i("Amplify.queryitems", "Got this many items from dynamo " + tasks.size());

                },
                error -> Log.i("Amplify.queryitems", "Did not get items")
        );



        recyclerView = findViewById(R.id.task_list_main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TaskAdapter(tasks, this));



        ApiOperation subscription = Amplify.API.subscribe(
                ModelSubscription.onCreate(Task.class),
                onEstablished -> Log.i("Amplify.Subscription", "Subscription established"),
                onCreated -> {
                    Log.i("Amplify.Subscription", "Todo create subscription received: " + ((Task) onCreated.getData()).getTitle());
                    Task newTask = (Task) onCreated.getData();
                    if (newTask.getTeam().getName().equals(preferences.getString("team", null))) {
                        tasks.add(newTask);
                        recyclerView.getAdapter().notifyItemInserted(1);
                    }
                },
                onFailure -> Log.e("Amplify.Subscription", "Subscription failed", onFailure),
                () -> Log.i("Amplify.Subscription", "Subscription completed")
        );

        Button loginButton = MainActivity.this.findViewById(R.id.loginMainActivityButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("should be moving to login activity");
                Intent goToLoginIntent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(goToLoginIntent);
            }
        });

        Button logoutButton = MainActivity.this.findViewById(R.id.logoutMainActivityButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.signOut(
                        () -> Log.i("AuthQuickstart", "Signed out successfully"),
                        error -> Log.e("AuthQuickstart", error.toString())
                );
            }
        });

        Button signUpButton = MainActivity.this.findViewById(R.id.signUpMainActivityButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("should be moving to sign up activity");
                Intent goToSignUpIntent = new Intent(MainActivity.this, SignUpActivity.class);
                MainActivity.this.startActivity(goToSignUpIntent);
            }
        });

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

//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String updatedText = preferences.getString("username", "") + " tasks";
        if (Amplify.Auth.getCurrentUser() != null) {
            TextView taskListTitle = findViewById(R.id.taskListTitleTextView);
            String updatedText = Amplify.Auth.getCurrentUser().getUsername() + " tasks";
            taskListTitle.setText(updatedText);
        }

//        db = Room.databaseBuilder(getApplicationContext(), Database.class, "jnelson_task_master")
//                .allowMainThreadQueries()
//                .build();
//        List<TaskInstance> tasks = db.taskInstanceDAO().getTasksSortByRecent();



        RecyclerView recyclerView = findViewById(R.id.task_list_main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TaskAdapter(tasks, this));
    }

    @Override
    public void taskListener(Task task) {
        Intent goToTaskDetailsIntent = new Intent(MainActivity.this, TaskDetail.class);
        goToTaskDetailsIntent.putExtra("title", task.getTitle());
        goToTaskDetailsIntent.putExtra("body", task.getBody());
        goToTaskDetailsIntent.putExtra("state", task.getState().getName());
        this.startActivity(goToTaskDetailsIntent);
    }

    public void addTestUser() {
        Amplify.Auth.signUp(
                "jack",
                "pass1234",
                AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), "jnelson.java@gmail.com").build(),
                result -> Log.i("AuthQuickStart", "Result: " + result.toString()),
                error -> Log.e("AuthQuickStart", "Sign up failed", error)
        );
    }

    public void verifyTestUser() {
        Amplify.Auth.confirmSignUp(
                "jack",
                "195622",
                result -> Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete"),
                error -> Log.e("AuthQuickstart", error.toString())
        );
    }

    public void signInTestUser() {
        Amplify.Auth.signIn(
                "jack",
                "pass1234",
                result -> Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete"),
                error -> Log.e("AuthQuickstart", error.toString())
        );
    }
}