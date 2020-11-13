package com.jnelsonjava.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.core.Amplify;

import static com.jnelsonjava.taskmaster.MainActivity.loggerE;
import static com.jnelsonjava.taskmaster.MainActivity.loggerI;

public class SignUpConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_confirmation);

        Intent intent = getIntent();

        Button confirmationButton = findViewById(R.id.confirmationNumberButton);
        confirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventTracker.trackButtonClicked(v);
                EditText confirmationNumber = findViewById(R.id.confirmationNumberEditText);
                Amplify.Auth.confirmSignUp(
                        intent.getExtras().getString("newUser"),
                        confirmationNumber.getText().toString(),
                        result -> loggerI("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete"),
                        error -> loggerE("AuthQuickstart", error.toString())
                );

                finish();
            }
        });
    }
}