package com.cactus.ui;

import android.content.Intent;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;

/***
 * Represents the activity responsible for displaying the signup page
 */
public class SignupActivity extends AppCompatActivity {

    @Inject
    UserInteractFacade userInteractFacade;

    /***
     * Logic for what to do when this activity is created
     *
     * On create, the signup options are initialized
     *
     * @param savedInstanceState state variable
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((CereusApplication) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("Cereus App : Signup");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        displayOptions();
    }

    /***
     * Display the signup text boxes and button
     */
    private void displayOptions(){
        EditText name = findViewById(R.id.name);
        EditText username = findViewById(R.id.newUsername);
        EditText password = findViewById(R.id.newPassword);
        Button signupButton = findViewById(R.id.button);

        signupButton.setOnClickListener(view -> {
            String givenName = name.getText().toString();
            String givenUsername = username.getText().toString();
            String givenPassword = password.getText().toString();

            if (userInteractFacade.createUser(givenName, givenUsername, givenPassword)){
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(SignupActivity.this, "The username already exists", Toast.LENGTH_LONG).show();
            }
        });
    }
}