package com.cactus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;

abstract public class AbstractActivity extends AppCompatActivity {

    @Inject
    UserInteractFacade userInteractFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((CereusApplication) getApplicationContext()).appComponent.inject(activity());
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

    abstract AbstractActivity activity();

    abstract void activitySetup();

    abstract void displayOptions();

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

    protected void homeButtonAction(){
        Intent intent = new Intent(this, DisplayingListsActivity.class);
        startActivity(intent);
    }

    protected void userButtonAction(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
