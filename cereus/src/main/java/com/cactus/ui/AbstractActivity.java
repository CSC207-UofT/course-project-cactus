package com.cactus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;

/**
 * Class responsible for the code that is shared for all activities
 */
abstract public class AbstractActivity extends AppCompatActivity {

    @Inject
    UserInteractFacade userInteractFacade;

    /**
     * Responsible for actions when the activity is created
     * @param savedInstanceState state variable
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((CereusApplication) getApplicationContext()).appComponent.inject(getActivity());
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // needs to be set before any networking is done
        // since the entry point to the app will pretty much always be here, set this here
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        activitySetup();
        displayOptions();
        toolbarAction();
    }

    /**
     * Getter for the current activity so this class can use it
     * @return current activity
     */
    abstract AbstractActivity getActivity();

    /**
     * Set up the activity before displaying
     */
    abstract void activitySetup();

    /**
     * Set up for view elements like buttons and text
     */
    abstract void displayOptions();

    /**
     * Define what the toolbar does
     */
    private void toolbarAction(){
        Button homeButton = findViewById(R.id.home_button);
        Button userButton = findViewById(R.id.user_button);

        homeButton.setOnClickListener(view -> {
            homeButtonAction();
        });

        userButton.setOnClickListener(view -> {
            userButtonAction();
        });
    }

    /**
     * Define what the home button does
     */
    protected void homeButtonAction(){
        Intent intent = new Intent(this, DisplayingListsActivity.class);
        startActivity(intent);
    }

    /**
     * Define what the user button does
     */
    protected void userButtonAction(){
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
}
