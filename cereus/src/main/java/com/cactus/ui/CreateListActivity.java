package com.cactus.ui;

import android.content.Intent;
import android.util.Log;
import android.widget.*;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;

/***
 * Represents the activity responsible for creating grocery lists
 */
public class CreateListActivity extends AbstractActivity{

    private final static String LOG_TAG = "CreateListActivity";

    private CustomItemSelectAdapter customItemSelectAdapter;

    @Override
    protected AbstractActivity activity(){
        return this;
    }

    @Override
    protected void activitySetup(){
        setContentView(R.layout.activity_creating_grocery_list);
        setTitle("Create List");

        try {
            customItemSelectAdapter = new CustomItemSelectAdapter(this, R.layout.selectable_template_layout,
                    this.userInteractFacade.getGroceryTemplateNames(), ((CereusApplication) getApplicationContext()).appComponent);
            ListView listView = findViewById(R.id.listViewDisplayTemplate);
            listView.setAdapter(customItemSelectAdapter);

            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        } catch (InvalidParamException | ServerException e) {
            Log.d(CreateListActivity.LOG_TAG, e.getMessage());
            Toast.makeText(CreateListActivity.this, e.getToastMessage(), Toast.LENGTH_LONG).show();
        }
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

            String selectedString = this.customItemSelectAdapter.getSelectedString();

            try {
                if (selectedString != null) {
                    this.userInteractFacade.newGroceryListWithTemplate(givenListName, selectedString);
                } else {
                    this.userInteractFacade.newGroceryList(givenListName, false);
                }

                Intent intent = new Intent(CreateListActivity.this, DisplayingListsActivity.class);
                startActivity(intent);
            } catch (InvalidParamException | ServerException e) {
                Log.d(CreateListActivity.LOG_TAG, e.getMessage());
                Toast.makeText(CreateListActivity.this, e.getToastMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
