package com.cactus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;
import java.util.ArrayList;

/***
 * Represents the activity responsible for choosing to create
 * or display grocery lists
 */
public class ListOptionsActivity extends AppCompatActivity {

    private ArrayList<String> items;

    @Inject
    UserInteractFacade userInteractFacade;

    /***
     * Logic for what to do when this activity is created
     *
     * On create, the buttons for list options are displayed
     *
     * @param savedInstanceState state variable
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((CereusApplication) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_options);
        setTitle("Cereus App : Choose Grocery List Option");

        displayOptions();
    }

    /***
     * Display the create new list button and display all lists button
     */
    private void displayOptions() {
        Button newListButton = findViewById(R.id.newListButton);
        Button allListsButton = findViewById(R.id.allListsButton);

        newListButton.setOnClickListener(view -> {
            Intent intent = new Intent(ListOptionsActivity.this, DisplayingItemsActivity.class);
            startActivity(intent);
        });

        allListsButton.setOnClickListener(view -> {
            Intent intent = new Intent(ListOptionsActivity.this, DisplayingListsActivity.class);
            startActivity(intent);
        });
    }

}
