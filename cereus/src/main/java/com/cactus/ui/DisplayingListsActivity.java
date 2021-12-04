package com.cactus.ui;

import android.content.Intent;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;

/***
 * Represents the activity responsible for displaying the grocery lists
 */
public class DisplayingListsActivity extends AbstractActivity {

    private final static String LOG_TAG = "DisplayingListsActivity";

    @Override
    protected AbstractActivity activity(){
        return this;
    }

    @Override
    protected void activitySetup(){
        setContentView(R.layout.activity_displaying_lists);

        setTitle(this.userInteractFacade.getName());

        try {
            CustomListAdapter listAdapter = new CustomListAdapter(this, R.layout.list_layout,
                    this.userInteractFacade.getGroceryListNames(), ((CereusApplication) getApplicationContext()).appComponent);
            ListView listView = findViewById(R.id.listViewDisplayList);

            listView.setAdapter(listAdapter);

            // display templates
            CustomListAdapter templateAdapter = new CustomListAdapter(this, R.layout.list_layout,
                    this.userInteractFacade.getGroceryTemplateNames(), ((CereusApplication) getApplicationContext()).appComponent);
            ListView templateView = findViewById(R.id.listViewDisplayTemplate);
            templateView.setAdapter(templateAdapter);
        } catch (InvalidParamException | ServerException e) {
            Log.d(DisplayingListsActivity.LOG_TAG, e.getMessage());
            Toast.makeText(DisplayingListsActivity.this, e.getToastMessage(), Toast.LENGTH_LONG).show();
        }

        displayOptions();
    }

    /***
     * Display the add list button
     */
    @Override
    protected void displayOptions() {
        Button addListButton = findViewById(R.id.addListButton);
        Button addTemplateButton = findViewById(R.id.addTemplateButton);

        addListButton.setOnClickListener(view -> {
            Intent intent = new Intent(DisplayingListsActivity.this, CreateListActivity.class);
            startActivity(intent);
        });

        addTemplateButton.setOnClickListener(view -> {
            Intent intent = new Intent(DisplayingListsActivity.this, CreateTemplateActivity.class);
            startActivity(intent);
        });

    }

}
