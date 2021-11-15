package com.cactus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.cactus.systems.UserInteractFacade;
import javax.inject.Inject;
import java.util.ArrayList;

/***
 * Represents the activity responsible for displaying the grocery items
 */
public class DisplayingItemsActivity extends AppCompatActivity {

    private ArrayList<String> items;

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
    protected void onCreate(Bundle savedInstanceState){
        ((CereusApplication) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaying_items);

        setTitle("Cereus App : " + this.userInteractFacade.getListName());

        items = userInteractFacade.getGroceryItemNames();
        CustomItemAdapter customItemAdapter = new CustomItemAdapter(this, R.layout.item_layout, items, ((CereusApplication) getApplicationContext()).appComponent);
        ListView listView = (ListView) findViewById(R.id.itemViewDisplayItem);
        listView.setAdapter(customItemAdapter);

        displayOptions(listView);
    }

    /***
     * Display the add item text field and button along with logout button
     *
     * @param listView listView layout variable
     */
    private void displayOptions(ListView listView){
        EditText itemName = findViewById(R.id.itemName);
        Button addItemButton = findViewById(R.id.addItemButton);
        Button logoutButton = findViewById(R.id.logoutButtonItem);

        addItemButton.setOnClickListener(view -> {
            String givenItemName = itemName.getText().toString();

            if (!items.contains(givenItemName)){
                items.add(givenItemName);
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                itemName.getText().clear();
            } else {
                Toast.makeText(DisplayingItemsActivity.this, "That name is already taken", Toast.LENGTH_LONG).show();
            }
        });

        logoutButton.setOnClickListener(view ->{
            if (!userInteractFacade.logout()) {
                Toast.makeText(DisplayingItemsActivity.this, "Logout failed", Toast.LENGTH_LONG).show();
            } else if (!this.userInteractFacade.addGroceryItems(items)){
                Toast.makeText(DisplayingItemsActivity.this, "Failed to save items", Toast.LENGTH_LONG).show();
            } else{
                Intent intent = new Intent(DisplayingItemsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /***
     * Logic for what to do when this activity is destroyed
     *
     * on delete, the items are saved to the database
     */
    @Override
    protected void onDestroy() {
        if (!this.userInteractFacade.addGroceryItems(items))
            Toast.makeText(DisplayingItemsActivity.this, "Failed to save items", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

}
