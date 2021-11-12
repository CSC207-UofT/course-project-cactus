package com.cactus.ui;

import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;
import java.util.ArrayList;

public class DisplayingItemsActivity extends AppCompatActivity {

    private EditText itemName;
    private Button addItemButton;

    @Inject
    UserInteractFacade userInteractFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        ((CereusApplication) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaying_items);

        itemName = findViewById(R.id.itemName);
        addItemButton = findViewById(R.id.addItemButton);

        ArrayList<String> items = userInteractFacade.getGroceryItemNames();
        CustomItemAdapter customItemAdapter = new CustomItemAdapter(this, R.layout.item_layout, items);
        ListView listView = (ListView) findViewById(R.id.itemViewDisplayItem);
        listView.setAdapter(customItemAdapter);

        addItemButton.setOnClickListener(view -> {
            String givenItemName = itemName.getText().toString();

            items.add(givenItemName);
            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
        });
    }

}
