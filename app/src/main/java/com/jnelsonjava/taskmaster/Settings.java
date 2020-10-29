package com.jnelsonjava.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    String team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        EditText usernameEditText = findViewById(R.id.editTextUsername);
        usernameEditText.setText(preferences.getString("username", "My Name"));

//        TextView taskListTitle = findViewById(R.id.taskListTitleTextView);
//        String updatedText = preferences.getString("username", "") + " tasks";
//        taskListTitle.setText(updatedText);

        team = preferences.getString("team", null);

        // check radio buttons for a match to current team and toggle it
        RadioGroup radioGroup = findViewById(R.id.userTeamAssignmentRadioGroup);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            if (radioButton.getText().toString().equals(team)) {
                radioButton.toggle();
            }
        }

        final SharedPreferences.Editor preferenceEditor = preferences.edit();

        findViewById(R.id.update_settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceEditor.putString("username", usernameEditText.getText().toString());
                preferenceEditor.putString("team", team);
                preferenceEditor.apply();

                Intent intent = new Intent(Settings.this, MainActivity.class);
                Settings.this.startActivity(intent);
                finish();
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        RadioButton radioButton = (RadioButton) view;
        team = radioButton.getText().toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(Settings.this, MainActivity.class);
        Settings.this.startActivity(intent);
        return true;
    }
}