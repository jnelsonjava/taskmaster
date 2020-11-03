package com.jnelsonjava.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button signUpButton = findViewById(R.id.signupActivitySubmitButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = findViewById(R.id.signUpUsernameEditText);
                EditText password = findViewById(R.id.signUpPasswordEditText);
                EditText email = findViewById(R.id.signUpEmailEditText);

                Amplify.Auth.signUp(
                        username.getText().toString(),
                        password.getText().toString(),
                        AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), email.getText().toString()).build(),
                        result -> Log.i("AuthQuickStart", "Result: " + result.toString()),
                        error -> Log.e("AuthQuickStart", "Sign up failed", error)
                );

                Intent confirmationIntent = new Intent(SignUpActivity.this, SignUpConfirmationActivity.class);
                confirmationIntent.putExtra("newUser", username.getText().toString());
                SignUpActivity.this.startActivity(confirmationIntent);
            }
        });
    }

    public void addTestUser() {
        Amplify.Auth.signUp(
                "jack",
                "asecurepassword",
                AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), "jnelson.java@gmail.com").build(),
                result -> Log.i("AuthQuickStart", "Result: " + result.toString()),
                error -> Log.e("AuthQuickStart", "Sign up failed", error)
        );
    }
}