package com.cactus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;
import java.util.ArrayList;

public class DisplayingItemsActivity extends AppCompatActivity {

    private EditText itemName;
    private Button addItemButton;
    private Button logoutButton;
    private ArrayList<String> items;

    @Inject
    UserInteractFacade userInteractFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        ((CereusApplication) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaying_items);

        setTitle("Cereus App : " + this.userInteractFacade.getListName());

        itemName = findViewById(R.id.itemName);
        addItemButton = findViewById(R.id.addItemButton);
        logoutButton = findViewById(R.id.logoutButtonItem);

        items = userInteractFacade.getGroceryItemNames();
        CustomItemAdapter customItemAdapter = new CustomItemAdapter(this, R.layout.item_layout, items, ((CereusApplication) getApplicationContext()).appComponent);
        ListView listView = (ListView) findViewById(R.id.itemViewDisplayItem);
        listView.setAdapter(customItemAdapter);

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
            if (userInteractFacade.logout()) {
                this.userInteractFacade.addGroceryItems(items);
                Intent intent = new Intent(DisplayingItemsActivity.this, MainActivity.class);
                startActivity(intent);
            } else{
                Toast.makeText(DisplayingItemsActivity.this, "Try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        this.userInteractFacade.addGroceryItems(items);
        super.onDestroy();
    }

}
