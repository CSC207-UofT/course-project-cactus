package com.cactus.ui;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;

/***
 * Represents the activity responsible for displaying the signup page
 */
public class SignupActivity extends AbstractActivity {

    private final static String LOG_TAG = "SignupActivity";

    @Override
    protected AbstractActivity activity(){
        return this;
    }

    @Override
    protected void activitySetup(){
        setContentView(R.layout.activity_sign_up);
        setTitle("Signup");

        // repurpose home button to be back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        Button backButton = toolbar.findViewById(R.id.home_button);
        backButton.setText("Back");

        // hide user button, since we're not logged in yet
        Button userButton = toolbar.findViewById(R.id.user_button);
        userButton.setVisibility(View.INVISIBLE);
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

            try {
                userInteractFacade.createUser(givenName, givenUsername, givenPassword);

                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
            } catch (InvalidParamException | ServerException e) {
                Log.d(SignupActivity.LOG_TAG, e.getMessage());
                Toast.makeText(SignupActivity.this, e.getToastMessage(), Toast.LENGTH_LONG).show();
            }
        });

        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void homeButtonAction(){
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
    }
}