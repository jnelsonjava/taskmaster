package com.jnelsonjava.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class AddTask extends AppCompatActivity {

    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // Thanks to David Dicken for helping out with Action Bar access!
        // And https://stackoverflow.com/questions/14545139/android-back-button-in-the-title-bar
        // And https://stackoverflow.com/questions/6867076/getactionbar-returns-null
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView submittedText = AddTask.this.findViewById(R.id.textSubmitted);

        Button addTaskButton = AddTask.this.findViewById(R.id.add_task_button2);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: change to toast
                submittedText.setVisibility(View.VISIBLE);

                db = Room.databaseBuilder(getApplicationContext(), Database.class, "jnelson_task_master")
                        .allowMainThreadQueries()
                        .build();

                // get info from form, create task, and save task to db
                EditText title = findViewById(R.id.editTextTaskTitle);
                EditText body = findViewById(R.id.editTextTaskDescription);

                Task task = new Task(title.getText().toString(), body.getText().toString(), "new");
                db.taskDAO().saveTask(task);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(AddTask.this, MainActivity.class);
        AddTask.this.startActivity(intent);
        return true;
    }
}