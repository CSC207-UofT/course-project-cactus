package com.cactus.ui;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/***
 * Represents the activity responsible for the login screen
 */
public class MainActivity extends AbstractActivity {

    @Override
    protected AbstractActivity activity(){
        return this;
    }

    @Override
    protected void activitySetup(){
        setContentView(R.layout.activity_main);
        setTitle("Login");
    }

    /***
     * Display the login options and signup button
     */
    @Override
    protected void displayOptions() {
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        Button signupButton = findViewById(R.id.signup);

        signupButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(view -> {
            String givenUsername = username.getText().toString();
            String givenPassword = password.getText().toString();

            if (userInteractFacade.login(givenUsername, givenPassword)) {
                Intent intent = new Intent(MainActivity.this, DisplayingListsActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(MainActivity.this, "Invalid username/password", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void homeButtonAction(){
        Toast.makeText(MainActivity.this, "Please Login first", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void userButtonAction(){
        Toast.makeText(MainActivity.this, "Please Login first", Toast.LENGTH_LONG).show();
    }
}