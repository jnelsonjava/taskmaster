package com.jnelsonjava.taskmaster;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.jnelsonjava.taskmaster.MainActivity.loggerE;
import static com.jnelsonjava.taskmaster.MainActivity.loggerI;


public class AddTask extends AppCompatActivity {

    String teamAssignment;
    Map<String, State> states;
    Map<String, Team> teams;
    File attachFile;
    String globalKey = "";

    FusedLocationProviderClient fusedLocationClient;
    Location currentLocation;
    String addressText;
    float lat;
    float lon;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        states = new HashMap<>();
        Amplify.API.query(
                ModelQuery.list(State.class),
                response -> {
                    for (State state : response.getData()) {
                        states.put(state.getName(), state);
                    }
                    loggerI("Amplify.queryitems", "Receveived states");
                },
                error -> loggerE("Amplify.queryitems", "Did not get states")
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


        Intent shareImageIntent = getIntent();
        if (shareImageIntent.getType() != null) {

            Uri imageUri = (Uri) shareImageIntent.getParcelableExtra(Intent.EXTRA_STREAM);

            attachFile = new File(getFilesDir(), "tempfile");

            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                FileUtils.copy(inputStream, new FileOutputStream(attachFile));
            } catch (IOException e) {
                e.printStackTrace();
            }

            TextView fileStatusText = AddTask.this.findViewById(R.id.selectedFileText);
            String fileAttached = "File Attached";
            fileStatusText.setText(fileAttached);
            loggerI("Amplify.imageAttach", "image attached");
        }

        // configure location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest locationRequest;
        LocationCallback locationCallback;

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                currentLocation = locationResult.getLastLocation();

                Geocoder geocoder = new Geocoder(AddTask.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 10);
                    lat = (float) currentLocation.getLatitude();
                    lon = (float) currentLocation.getLongitude();
                    addressText = addresses.get(0).getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            loggerI("Location", "Location access permission is not granted");

            requestLocationAccess();
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());

        Button selectFileButton = AddTask.this.findViewById(R.id.chooseFileButton);
        selectFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventTracker.trackButtonClicked(v);
                retrieveFile();
            }
        });

        Button addTaskButton = AddTask.this.findViewById(R.id.add_task_button2);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventTracker.trackButtonClicked(v);
                Toast.makeText(AddTask.this, "Submitted!", Toast.LENGTH_SHORT).show();

                EditText title = findViewById(R.id.editTextTaskTitle);
                EditText body = findViewById(R.id.editTextTaskDescription);

                if (attachFile.exists()) {
                    globalKey = attachFile.getName() + Math.random();
                    uploadFile(attachFile, globalKey);
                }

                Task task = Task.builder()
                        .stateId(states.get("new").getId())
                        .title(title.getText().toString())
                        .body(body.getText().toString())
                        .filekey(globalKey)
                        .address(addressText)
                        .lat(lat).lon(lon)
                        .team(teams.get(teamAssignment))
                        .build();

                Amplify.API.mutate(ModelMutation.create(task),
                        response -> loggerI("AddTaskActivityAmplify", "successfully added task"),
                        error -> loggerE("AddTaskActivityAmplify", error.toString()));
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        EventTracker.trackButtonClicked(view);
        RadioButton radioButton = (RadioButton) view;
        teamAssignment = radioButton.getText().toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        EventTracker.trackButtonClicked((View) item);
        Intent intent = new Intent(AddTask.this, MainActivity.class);
        AddTask.this.startActivity(intent);
        return true;
    }

    public void retrieveFile() {
        Intent getPictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getPictureIntent.setType("*/*");

        startActivityForResult(getPictureIntent, 987); // request code is custom
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 987) {
            loggerI("Amplify.resultImage", "image pick process returned");
            updateAttachFile(data);
        } else {
            loggerI("Amplify.resultActivity", "the request code does not match any expected");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void updateAttachFile(Intent data) {
        attachFile = new File(getFilesDir(), "tempfile");

        try {
            InputStream inputStream = getContentResolver().openInputStream(data.getData());
            FileUtils.copy(inputStream, new FileOutputStream(attachFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView fileStatusText = AddTask.this.findViewById(R.id.selectedFileText);
        String fileAttached = "File Attached";
        fileStatusText.setText(fileAttached);
        loggerI("Amplify.imageAttach", "image attached");
    }

    public void uploadFile(File file, String fileKey) {
        Amplify.Storage.uploadFile(
                fileKey,
                file,
                result -> loggerI("Amplify.S3", "Successfully uploaded: " + result.getKey()),
                storageFailure -> loggerE("Amplify.S3", "Upload failed", storageFailure)
        );
    }

    public void requestLocationAccess() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
    }
}