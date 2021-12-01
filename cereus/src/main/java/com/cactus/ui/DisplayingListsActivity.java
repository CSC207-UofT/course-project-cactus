package com.cactus.ui;

import android.content.Intent;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;

/***
 * Represents the activity responsible for displaying the grocery lists
 */
public class DisplayingListsActivity extends AppCompatActivity {

    @Inject
    UserInteractFacade userInteractFacade;

    /***
     * Logic for what to do when this activity is created
     *
     * On create, the list, buttons, and text fields are initialized
     *
     * @param savedInstanceState state variable
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((CereusApplication) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaying_lists);

        setTitle(this.userInteractFacade.getUserName());

        CustomListAdapter customListAdapter = new CustomListAdapter(this, R.layout.list_layout,
                this.userInteractFacade.getGroceryListNames(), ((CereusApplication) getApplicationContext()).appComponent);
        ListView listView = findViewById(R.id.listViewDisplayList);
        listView.setAdapter(customListAdapter);

        displayOptions(listView, customListAdapter);
    }

    /***
     * Display the add list text field and button along with logout button
     *
     * @param listView listView layout variable
     * @param customListAdapter customListAdapter for displaying list
     */
    private void displayOptions(ListView listView, CustomListAdapter customListAdapter) {
        EditText listName = findViewById(R.id.listName);
        Button createListButton = findViewById(R.id.addListButton);

        createListButton.setOnClickListener(view -> {
            String givenListName = listName.getText().toString();
            if (this.userInteractFacade.newGroceryList(givenListName)) {
                customListAdapter.objects.add(givenListName);
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                listName.getText().clear();
            } else {
                Toast.makeText(DisplayingListsActivity.this, "That name is already taken", Toast.LENGTH_LONG).show();
            }
        });

    }

}
