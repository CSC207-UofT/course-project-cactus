package com.cactus.ui;

import android.content.Intent;
import android.util.Log;
import android.widget.*;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;

import java.util.ArrayList;

/***
 * Class responsible for the create templates page
 */
public class CreateTemplateActivity extends AbstractActivity {

    private final static String LOG_TAG = "CreateTemplateActivity";

    private ArrayList<String> items;
    private ListView listView;

    /**
     * Getter for the current activity so the abstract class can use it
     * @return current activity
     */
    @Override
    protected AbstractActivity getActivity() {
        return this;
    }

    /**
     * Set up the activity before displaying
     */
    @Override
    protected void activitySetup() {
        setContentView(R.layout.activity_creating_template);

        setTitle("Create Template");

        items = new ArrayList<>();
        CustomItemAdapter customItemAdapter = new CustomItemAdapter(this, R.layout.item_layout, items, ((CereusApplication) getApplicationContext()).appComponent);
        listView = findViewById(R.id.itemViewDisplayItem);
        listView.setAdapter(customItemAdapter);
    }

    /**
     * Set up for view elements like buttons and text
     */
    @Override
    protected void displayOptions() {
        EditText templateName = findViewById(R.id.templateName);
        EditText itemName = findViewById(R.id.itemName);
        Button addTemplateButton = findViewById(R.id.addTemplateButton);
        Button addItemButton = findViewById(R.id.addItemButton);

        addItemButton.setOnClickListener(view -> {
            String givenItemName = itemName.getText().toString();

            if (!items.contains(givenItemName)) {
                items.add(givenItemName);
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                itemName.getText().clear();
            } else {
                Toast.makeText(CreateTemplateActivity.this, "Item already taken", Toast.LENGTH_LONG).show();
            }
        });

        addTemplateButton.setOnClickListener(view -> {
            String givenTemplateName = templateName.getText().toString();

            try {
                this.userInteractFacade.newGroceryList(givenTemplateName, true);
                this.userInteractFacade.addGroceryItems(items);
            } catch (InvalidParamException | ServerException e) {
                Log.d(CreateTemplateActivity.LOG_TAG, e.getMessage());
                Toast.makeText(CreateTemplateActivity.this, e.getToastMessage(), Toast.LENGTH_LONG).show();
            }

            Intent intent = new Intent(CreateTemplateActivity.this, DisplayingListsActivity.class);
            startActivity(intent);
        });
    }
}
