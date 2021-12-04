package com.cactus.ui;

import android.content.Intent;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;
import java.util.ArrayList;

/***
 * Represents the activity responsible for creating templates
 */
public class CreateTemplateActivity extends AppCompatActivity{

    private ArrayList<String> items;

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
        setContentView(R.layout.activity_creating_template);

        setTitle("Create Template");

        items = new ArrayList<String>();
        CustomItemAdapter customItemAdapter = new CustomItemAdapter(this, R.layout.item_layout, items, ((CereusApplication) getApplicationContext()).appComponent);
        ListView listView = findViewById(R.id.itemViewDisplayItem);
        listView.setAdapter(customItemAdapter);

        displayOptions(listView);
    }

    /***
     * Display the add template text field and button
     *
     * @param listView listView layout variable
     */
    private void displayOptions(ListView listView) {
        EditText templateName = findViewById(R.id.templateName);
        EditText itemName = findViewById(R.id.itemName);
        Button addTemplateButton = findViewById(R.id.addTemplateButton);
        Button addItemButton = findViewById(R.id.addItemButton);

        addItemButton.setOnClickListener(view -> {
            String givenItemName = itemName.getText().toString();

            if (!items.contains(givenItemName)){
                items.add(givenItemName);
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                itemName.getText().clear();
            } else {
                Toast.makeText(CreateTemplateActivity.this, "That name is already taken", Toast.LENGTH_LONG).show();
            }
        });

        addTemplateButton.setOnClickListener(view -> {
            String givenTemplateName = templateName.getText().toString();

            if (this.userInteractFacade.newGroceryList(givenTemplateName, true)){
                this.userInteractFacade.setCurrentGroceryList(givenTemplateName);
                this.userInteractFacade.addGroceryItems(items);
                Intent intent = new Intent(CreateTemplateActivity.this, DisplayingListsActivity.class);
                startActivity(intent);
            }
        });
    }
}
