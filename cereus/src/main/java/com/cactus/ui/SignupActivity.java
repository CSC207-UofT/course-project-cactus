package com.cactus.ui;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/***
 * Represents the activity responsible for displaying the signup page
 */
public class SignupActivity extends AbstractActivity {

    @Override
    protected AbstractActivity activity(){
        return this;
    }

    @Override
    protected void activitySetup(){
        setContentView(R.layout.activity_sign_up);
        setTitle("Signup");
    }

    /***
     * Display the signup text boxes and button
     */
    @Override
    protected void displayOptions() {
        EditText name = findViewById(R.id.name);
        EditText username = findViewById(R.id.newUsername);
        EditText password = findViewById(R.id.newPassword);
        Button signupButton = findViewById(R.id.signup_button);
        Button loginButton = findViewById(R.id.login_button);

        signupButton.setOnClickListener(view -> {
            String givenName = name.getText().toString();
            String givenUsername = username.getText().toString();
            String givenPassword = password.getText().toString();

            if (userInteractFacade.createUser(givenName, givenUsername, givenPassword)) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(SignupActivity.this, "The username already exists", Toast.LENGTH_LONG).show();
            }
        });

        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void homeButtonAction(){
        Toast.makeText(SignupActivity.this, "Please Login first", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void userButtonAction(){
        Toast.makeText(SignupActivity.this, "Please Login first", Toast.LENGTH_LONG).show();
    }
}