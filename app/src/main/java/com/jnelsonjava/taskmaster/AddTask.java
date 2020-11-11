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


public class AddTask extends AppCompatActivity {

    //    Database db;
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


        // handle image shared from external app
        Intent shareImageIntent = getIntent();
//        Log.i("getDataStatus", shareImageIntent.getType().toString());
        if (shareImageIntent.getType() != null) {

            // reference for parsing image intent for Uri https://code.tutsplus.com/tutorials/android-sdk-receiving-data-from-the-send-intent--mobile-14878
            Uri imageUri = (Uri) shareImageIntent.getParcelableExtra(Intent.EXTRA_STREAM);

            attachFile = new File(getFilesDir(), "tempfile");

            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                FileUtils.copy(inputStream, new FileOutputStream(attachFile));
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("does it exist" + attachFile.exists());
            TextView fileStatusText = AddTask.this.findViewById(R.id.selectedFileText);
            String fileAttached = "File Attached";
            fileStatusText.setText(fileAttached);
            Log.i("Amplify.imageAttach", "image attached");
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
//                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                currentLocation = locationResult.getLastLocation();
                Log.i("Location", currentLocation.toString());

                Geocoder geocoder = new Geocoder(AddTask.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 10);
                    Log.i("Location", addresses.get(0).toString());
                    lat = (float) currentLocation.getLatitude();
                    lon = (float) currentLocation.getLongitude();
                    addressText = addresses.get(0).getAddressLine(0);
                    Log.i("Location", addressText);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Location", "Location access permission is not granted");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        fusedLocationClient.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null) {
//                            // TODO: handle location object
//                        }
//                    }
//                });
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

//                db = Room.databaseBuilder(getApplicationContext(), Database.class, "jnelson_task_master")
//                        .allowMainThreadQueries()
//                        .build();

                System.out.println(teamAssignment);
                System.out.println(states);
                System.out.println(teams);

                // get info from form, create task, and save task to db
                EditText title = findViewById(R.id.editTextTaskTitle);
                EditText body = findViewById(R.id.editTextTaskDescription);

                if (attachFile.exists()) {
                    globalKey = attachFile.getName() + Math.random();
                    uploadFile(attachFile, globalKey);
                }

//                Task task = new Task(title.getText().toString(), body.getText().toString(), "new");
//                db.taskInstanceDAO().saveTask(task);


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
                        response -> Log.i("AddTaskActivityAmplify", "successfully added task"),
                        error -> Log.e("AddTaskActivityAmplify", error.toString()));
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        EventTracker.trackButtonClicked(view);
        RadioButton radioButton = (RadioButton) view;
        teamAssignment = radioButton.getText().toString();
//        System.out.println("TeamRadioButtonChecked: " + teamAssignment);
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

        // below is a sample of getting specific filetypes
//        getPictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{".png",".jpg",".PNG"});

        // this makes files immediately accessible
//        getPictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
//        getPictureIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        // opens file picker with files matching type
//        startActivity(getPictureIntent);

        startActivityForResult(getPictureIntent, 987); // request code is custom
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 987) {
            Log.i("Amplify.resultImage", "image pick process returned");
            updateAttachFile(data);
        } else {
            Log.i("Amplify.resultActivity", "the request code does not match any expected");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void updateAttachFile(Intent data) {
        attachFile = new File(getFilesDir(), "tempfile");

        System.out.println(data.getDataString());

        try {
            InputStream inputStream = getContentResolver().openInputStream(data.getData());
            FileUtils.copy(inputStream, new FileOutputStream(attachFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("does it exist" + attachFile.exists());
        TextView fileStatusText = AddTask.this.findViewById(R.id.selectedFileText);
        String fileAttached = "File Attached";
        fileStatusText.setText(fileAttached);
        Log.i("Amplify.imageAttach", "image attached");
    }

    public void uploadFile(File file, String fileKey) {
        Amplify.Storage.uploadFile(
                fileKey,
                file,
                result -> Log.i("Amplify.S3", "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e("Amplify.S3", "Upload failed", storageFailure)
        );
    }
}