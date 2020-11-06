package com.jnelsonjava.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;

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
                            Log.i("AuthQuickstart", signInStatus);
                        },
                        error -> Log.e("AuthQuickstart", error.toString())
                );
            }
        });
    }
}