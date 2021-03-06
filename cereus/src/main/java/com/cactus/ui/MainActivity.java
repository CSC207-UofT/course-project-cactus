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
 * Class responsible for the login page
 */
public class MainActivity extends AbstractActivity {

    private final static String LOG_TAG = "MainActivity";

    /**
     * Getter for the current activity so the abstract class can use it
     * @return current activity
     */
    @Override
    protected AbstractActivity getActivity(){
        return this;
    }

    /**
     * Set up the activity before displaying
     */
    @Override
    protected void activitySetup(){
        setContentView(R.layout.activity_main);
        setTitle("Login");

        // hide user and home buttons, since we're not logged in yet
        Toolbar toolbar = findViewById(R.id.toolbar);
        Button userButton = toolbar.findViewById(R.id.user_button);
        userButton.setVisibility(View.INVISIBLE);

        Button homeButton = toolbar.findViewById(R.id.home_button);
        homeButton.setVisibility(View.INVISIBLE);
    }

    /**
     * Set up for view elements like buttons and text
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

            password.setText("");

            try {
                userInteractFacade.login(givenUsername, givenPassword);

                username.setText("");

                Intent intent = new Intent(MainActivity.this, DisplayingListsActivity.class);
                startActivity(intent);

            } catch (InvalidParamException | ServerException e){
                Log.d(MainActivity.LOG_TAG, e.getMessage());
                Toast.makeText(MainActivity.this, e.getToastMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}