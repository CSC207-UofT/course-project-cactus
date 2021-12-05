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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * Represents the activity responsible for displaying the grocery lists
 */
public class DisplayingListsActivity extends AbstractActivity {

    private final static String LOG_TAG = "DisplayingListsActivity";

    List<String> listNames;
    List<String> templateNames;

    ListView listView;
    ListView templateView;

    @Override
    protected AbstractActivity activity(){
        return this;
    }

    @Override
    protected void activitySetup(){
        setContentView(R.layout.activity_displaying_lists);

        setTitle(this.userInteractFacade.getName());

        this.listNames = new ArrayList<>();
        this.templateNames = new ArrayList<>();

        CustomListAdapter listAdapter = new CustomListAdapter(this, R.layout.list_layout, this.listNames , ((CereusApplication) getApplicationContext()).appComponent);
        this.listView = findViewById(R.id.listViewDisplayList);
        listView.setAdapter(listAdapter);

        // display templates
        CustomListAdapter templateAdapter = new CustomListAdapter(this, R.layout.list_layout, this.templateNames, ((CereusApplication) getApplicationContext()).appComponent);
        this.templateView = findViewById(R.id.listViewDisplayTemplate);
        this.templateView.setAdapter(templateAdapter);

        displayOptions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetch();

        ((BaseAdapter) this.listView.getAdapter()).notifyDataSetChanged();
        ((BaseAdapter) this.templateView.getAdapter()).notifyDataSetChanged();
    }

    /**
     * Fetch from server
     */
    private void fetch() {
        try {
            // do this to not overwrite the object so the adapters can see updates
            this.listNames.clear();
            this.listNames.addAll(this.userInteractFacade.getGroceryListNamesForce());

            this.templateNames.clear();
            this.templateNames.addAll(this.userInteractFacade.getGroceryTemplateNamesForce());

        } catch (InvalidParamException | ServerException e) {
            Log.d(DisplayingListsActivity.LOG_TAG, e.getMessage());
            Toast.makeText(DisplayingListsActivity.this, e.getToastMessage(), Toast.LENGTH_LONG).show();
        }
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
