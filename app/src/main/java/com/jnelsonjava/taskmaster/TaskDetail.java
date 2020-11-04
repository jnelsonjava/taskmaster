package com.jnelsonjava.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;

import java.io.File;

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

        taskTitle.setText(intent.getExtras().getString("title"));
        taskBody.setText(intent.getExtras().getString("body"));
        String stateText = "Progress: " + intent.getExtras().getString("state");
        taskState.setText(stateText);

        downloadFile("happyFile");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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