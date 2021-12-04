package com.cactus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import java.util.ArrayList;

/***
 * Represents the activity responsible for displaying the grocery items
 */
public class DisplayingItemsActivity extends AbstractActivity {

    private ArrayList<String> items;
    private ListView listView;

    @Override
    protected AbstractActivity activity(){
        return this;
    }

    @Override
    protected void activitySetup(){
        setContentView(R.layout.activity_displaying_items);

        setTitle(this.userInteractFacade.getListName());

        items = userInteractFacade.getGroceryItemNames();
        CustomItemAdapter customItemAdapter = new CustomItemAdapter(this, R.layout.item_layout, items, ((CereusApplication) getApplicationContext()).appComponent);
        listView = findViewById(R.id.itemViewDisplayItem);
        listView.setAdapter(customItemAdapter);
    }

    /***
     * Display the add item text field and button along with logout button
     */
    @Override
    protected void displayOptions() {
        EditText itemName = findViewById(R.id.itemName);
        Button addItemButton = findViewById(R.id.addItemButton);
//        Button logoutButton = findViewById(R.id.logoutButtonItem);

        addItemButton.setOnClickListener(view -> {
            String givenItemName = itemName.getText().toString();

            if (!items.contains(givenItemName)) {
                items.add(givenItemName);
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                itemName.getText().clear();
            } else {
                Toast.makeText(DisplayingItemsActivity.this, "That name is already taken", Toast.LENGTH_LONG).show();
            }
        });

//        logoutButton.setOnClickListener(view ->{
//            if (!this.userInteractFacade.addGroceryItems(items)) {
//                Toast.makeText(DisplayingItemsActivity.this, "Failed to save items", Toast.LENGTH_LONG).show();
//            } else if (!userInteractFacade.logout()) {
//                Toast.makeText(DisplayingItemsActivity.this, "Logout failed", Toast.LENGTH_LONG).show();
//            } else {
//                Intent intent = new Intent(DisplayingItemsActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    /**
     * Logic for what to do when this activity is destroyed
     * <p>
     * on delete, the items are saved to the database
     */
    @Override
    protected void onDestroy() {
        if (!this.userInteractFacade.addGroceryItems(items))
            Toast.makeText(DisplayingItemsActivity.this, "Failed to save items", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

}
