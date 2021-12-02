package com.cactus.ui;

import android.content.Intent;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;

/***
 * Represents the activity responsible for creating grocery lists
 */
public class CreateListActivity extends AppCompatActivity{

    @Inject
    UserInteractFacade userInteractFacade;

    /***
     * Logic for what to do when this activity is created
     *
     * On create, the buttons, and text fields are initialized
     *
     * @param savedInstanceState state variable
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((CereusApplication) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_grocery_list);

        setTitle("Create List");

        CustomItemSelectAdapter customItemSelectAdapter = new CustomItemSelectAdapter(this, R.layout.selectable_template_layout,
                this.userInteractFacade.getGroceryListNames(), ((CereusApplication) getApplicationContext()).appComponent);
        ListView listView = findViewById(R.id.listViewDisplayTemplate);
        listView.setAdapter(customItemSelectAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        displayOptions();
    }

    /***
     * Display the add list text field and button
     */
    private void displayOptions() {
        EditText listName = findViewById(R.id.listName);
        Button createListButton = findViewById(R.id.addListButton);

        createListButton.setOnClickListener(view -> {
            String givenListName = listName.getText().toString();
            if (this.userInteractFacade.newGroceryList(givenListName)) {
                Intent intent = new Intent(CreateListActivity.this, DisplayingListsActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(CreateListActivity.this, "That name is already taken", Toast.LENGTH_LONG).show();
            }
        });

    }
}
