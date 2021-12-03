package com.cactus.ui;

import android.content.Intent;
import android.widget.*;

import java.util.ArrayList;

/***
 * Represents the activity responsible for creating templates
 */
public class CreateTemplateActivity extends AbstractActivity{

    private ArrayList<String> items;
    private ListView listView;

    @Override
    protected AbstractActivity activity(){
        return this;
    }

    @Override
    protected void activitySetup(){
        setContentView(R.layout.activity_creating_template);

        setTitle("Create Template");

        items = new ArrayList<String>();
        CustomItemAdapter customItemAdapter = new CustomItemAdapter(this, R.layout.item_layout, items, ((CereusApplication) getApplicationContext()).appComponent);
        listView = findViewById(R.id.itemViewDisplayItem);
        listView.setAdapter(customItemAdapter);
    }

    /***
     * Display the add template text field and button
     */
    @Override
    protected void displayOptions() {
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

            if (this.userInteractFacade.newGroceryList(givenTemplateName)){
                this.userInteractFacade.setCurrentGroceryList(givenTemplateName);
                this.userInteractFacade.addGroceryItems(items);
                Intent intent = new Intent(CreateTemplateActivity.this, DisplayingListsActivity.class);
                startActivity(intent);
            }
        });
    }
}
