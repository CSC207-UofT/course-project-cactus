package com.cactus.ui;

import android.util.Log;
import android.widget.*;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;

import java.util.List;

/***
 * Represents the activity responsible for displaying the grocery items
 */
public class DisplayingItemsActivity extends AbstractActivity {

    private final static String LOG_TAG = "DisplayItemsActivity";

    private List<String> items;
    private ListView listView;

    @Override
    protected AbstractActivity activity() {
        return this;
    }

    @Override
    protected void activitySetup() {
        setContentView(R.layout.activity_displaying_items);

        setTitle(this.userInteractFacade.getListName());


        try {
            items = userInteractFacade.getGroceryItemNames();

        } catch (InvalidParamException | ServerException e) {
            Log.d(DisplayingItemsActivity.LOG_TAG, e.getMessage());
            Toast.makeText(DisplayingItemsActivity.this, e.getToastMessage(), Toast.LENGTH_LONG).show();
        }

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
        Button shareButton = findViewById(R.id.share_link);

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

        shareButton.setOnClickListener(view ->{
            // TODO: fill in
        });
    }

    /**
     * Logic for what to do when this activity is destroyed
     * <p>
     * on delete, the items are saved to the database
     */
    @Override
    protected void onDestroy() {
        try {
            userInteractFacade.addGroceryItems(items);

        } catch (InvalidParamException | ServerException e) {
            Log.d(DisplayingItemsActivity.LOG_TAG, e.getMessage());
            Toast.makeText(DisplayingItemsActivity.this, e.getToastMessage(), Toast.LENGTH_LONG).show();
        }

        super.onDestroy();
    }

}
