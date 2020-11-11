package com.jnelsonjava.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;

import java.io.File;
import java.util.Locale;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        TextView taskTitle = TaskDetail.this.findViewById(R.id.taskTitleTextView);
        TextView taskBody = TaskDetail.this.findViewById(R.id.taskBodyTextView);
        TextView taskState = TaskDetail.this.findViewById(R.id.taskStateTextView);
        TextView taskAddress = TaskDetail.this.findViewById(R.id.taskAddressTextView);

        taskTitle.setText(intent.getExtras().getString("title"));
        taskBody.setText(intent.getExtras().getString("body"));
        String stateText = "Progress: " + intent.getExtras().getString("state");
        taskState.setText(stateText);
        taskAddress.setText(intent.getExtras().getString("address"));

        downloadFile(intent.getExtras().getString("filekey"));

        Button mapButton = findViewById(R.id.mapViewButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // reference for String.format with floats https://www.geeksforgeeks.org/java-string-format-examples/
                String location = String.format("geo:%.4f,%.4f", intent.getExtras().getFloat("lat"), intent.getExtras().getFloat("lon"));
                // reference for starting map activity https://developers.google.com/maps/documentation/urls/android-intents
                Uri mapIntentUri = Uri.parse(location);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        EventTracker.trackButtonClicked((View) item);
        Intent intent = new Intent(TaskDetail.this, MainActivity.class);
        TaskDetail.this.startActivity(intent);
        return true;
    }

    private void downloadFile(String fileKey) {
        Amplify.Storage.downloadFile(
                fileKey,
                new File(getApplicationContext().getFilesDir() + "/" + fileKey + ".txt"),
                result -> {
                    Log.i("Amplify.S3download", "Successfully downloaded: " + result.getFile().getName());



                    ImageView taskImage = findViewById(R.id.taskDetailImage);
                    taskImage.setImageBitmap(BitmapFactory.decodeFile(result.getFile().getPath()));
                },
                error -> Log.e("Amplify.S3download",  "Download Failure", error)
        );
    }
}