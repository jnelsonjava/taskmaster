package com.jnelsonjava.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.core.Amplify;

import static com.jnelsonjava.taskmaster.MainActivity.loggerE;
import static com.jnelsonjava.taskmaster.MainActivity.loggerI;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.loginActivitySubmitButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventTracker.trackButtonClicked(v);
                EditText username = findViewById(R.id.loginUsernameEditText);
                EditText password = findViewById(R.id.loginPasswordEditText);

                Amplify.Auth.signIn(
                        username.getText().toString(),
                        password.getText().toString(),
                        result -> {
                            String signInStatus = result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete";
                            loggerI("AuthQuickstart", signInStatus);
                        },
                        error -> loggerE("AuthQuickstart", error.toString())
                );
            }
        });
    }
}