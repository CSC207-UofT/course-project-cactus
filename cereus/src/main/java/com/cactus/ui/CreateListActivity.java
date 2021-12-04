package com.cactus.ui;

import android.content.Intent;
import android.widget.*;

/***
 * Represents the activity responsible for creating grocery lists
 */
public class CreateListActivity extends AbstractActivity{

    private CustomItemSelectAdapter customItemSelectAdapter;

    @Override
    protected AbstractActivity activity(){
        return this;
    }

    @Override
    protected void activitySetup(){
        setContentView(R.layout.activity_creating_grocery_list);
        setTitle("Create List");

        customItemSelectAdapter = new CustomItemSelectAdapter(this, R.layout.selectable_template_layout,
                this.userInteractFacade.getGroceryTemplateNames(), ((CereusApplication) getApplicationContext()).appComponent);
        ListView listView = findViewById(R.id.listViewDisplayTemplate);
        listView.setAdapter(customItemSelectAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    /***
     * Display the add list text field and button
     */
    @Override
    protected void displayOptions() {
        EditText listName = findViewById(R.id.listName);
        Button createListButton = findViewById(R.id.addListButton);

        createListButton.setOnClickListener(view -> {

            String givenListName = listName.getText().toString();

            boolean success;

            String selectedString = this.customItemSelectAdapter.getSelectedString();

            if (selectedString != null) {
                success = this.userInteractFacade.newGroceryListWithTemplate(givenListName, selectedString);
            } else {
                success = this.userInteractFacade.newGroceryList(givenListName, false);
            }

            if (success) {
                Intent intent = new Intent(CreateListActivity.this, DisplayingListsActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(CreateListActivity.this, "That name is already taken", Toast.LENGTH_LONG).show();
            }
        });

    }
}
