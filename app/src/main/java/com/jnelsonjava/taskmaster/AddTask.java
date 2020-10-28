package com.jnelsonjava.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.State;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddTask extends AppCompatActivity {

//    Database db;
    String teamAssignment;
    Map<String, State> states;
    Map<String, Team> teams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // Thanks to David Dicken for helping out with Action Bar access!
        // And https://stackoverflow.com/questions/14545139/android-back-button-in-the-title-bar
        // And https://stackoverflow.com/questions/6867076/getactionbar-returns-null
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // query dynamo for available states
        states = new HashMap<>();
        Amplify.API.query(
                ModelQuery.list(State.class),
                response -> {
                    for (State state : response.getData()) {
                        states.put(state.getName(), state);
                    }
                    Log.i("Amplify.queryitems", "Receveived states");
                },
                error -> Log.e("Amplify.queryitems", "Did not get states")
        );

        // query dynamo for available teams
        teams = new HashMap<>();
        Amplify.API.query(
                ModelQuery.list(Team.class),
                response -> {
                    for (Team team : response.getData()) {
                        teams.put(team.getName(), team);
                    }
                    Log.i("Amplify.queryitems", "Receveived teams");
                },
                error -> Log.e("Amplify.queryitems", "Did not get teams")
        );


        Button addTaskButton = AddTask.this.findViewById(R.id.add_task_button2);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddTask.this, "Submitted!", Toast.LENGTH_SHORT).show();

//                db = Room.databaseBuilder(getApplicationContext(), Database.class, "jnelson_task_master")
//                        .allowMainThreadQueries()
//                        .build();

                System.out.println(teamAssignment);
                System.out.println(states);
                System.out.println(teams);

                // get info from form, create task, and save task to db
                EditText title = findViewById(R.id.editTextTaskTitle);
                EditText body = findViewById(R.id.editTextTaskDescription);


//                Task task = new Task(title.getText().toString(), body.getText().toString(), "new");
//                db.taskInstanceDAO().saveTask(task);


                Task task = Task.builder()
                        .stateId(states.get("new").getId())
                        .title(title.getText().toString())
                        .body(body.getText().toString())
                        .team(teams.get(teamAssignment))
                        .build();

                Amplify.API.mutate(ModelMutation.create(task),
                        response -> Log.i("AddTaskActivityAmplify", "successfully added task"),
                        error -> Log.e("AddTaskActivityAmplify", error.toString()));

                // LEAVE THIS COMMENTED OUT, TEMPORARY USAGE FOR STATIC TABLE CREATION
//                Team team = Team.builder()
//                        .name("Zorg")
//                        .build();
//
//                Amplify.API.mutate(ModelMutation.create(team),
//                        response -> Log.i("Amplify.AddTeam", "successfully added team"),
//                        error -> Log.e("Amplify.AddTeam", error.toString())
//                );
                // END OF COMMENT SECTION

            }
        });
    }

    public void onRadioButtonClicked(View view) {
        RadioButton radioButton = (RadioButton) view;
        teamAssignment = radioButton.getText().toString();
//        System.out.println("TeamRadioButtonChecked: " + teamAssignment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(AddTask.this, MainActivity.class);
        AddTask.this.startActivity(intent);
        return true;
    }
}