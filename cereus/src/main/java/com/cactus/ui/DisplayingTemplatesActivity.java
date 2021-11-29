package com.cactus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;

/***
 * Represents the activity responsible for displaying the grocery lists
 */
public class DisplayingTemplatesActivity extends AppCompatActivity {
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
    protected void onCreate(Bundle savedInstanceState) {
        ((CereusApplication) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaying_templates);

        setTitle("Cereus App : " + this.userInteractFacade.getUserName());

        CustomListAdapter customListAdapter = new CustomListAdapter(this, R.layout.list_layout,
                this.userInteractFacade.getTemplateNames(), ((CereusApplication) getApplicationContext()).appComponent);
        ListView listView = findViewById(R.id.templateViewDisplayTemplate);
        listView.setAdapter(customListAdapter);

        displayOptions(listView, customListAdapter);
    }

    /***
     * Display the add list text field and button along with logout button
     *
     * @param listView listView layout variable
     * @param customListAdapter customListAdapter for displaying list
     */
    private void displayOptions(ListView listView, CustomListAdapter customListAdapter) {
        EditText listName = findViewById(R.id.listName);
        Button addTemplateButton = findViewById(R.id.addTemplateButton);
        Button logoutButton = findViewById(R.id.logoutButtonTemplate);
        Button defaultTempButton = findViewById(R.id.defaultTemplateButton);

        defaultTempButton.setOnClickListener(view -> {
            Intent intent = new Intent(DisplayingTemplatesActivity.this,
                    DisplayingListsActivity.class);
            startActivity(intent);
        });

        addTemplateButton.setOnClickListener(view -> {
            String givenListName = listName.getText().toString();
            if (this.userInteractFacade.newTemplate(givenListName)) {
                customListAdapter.objects.add(givenListName);
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                listName.getText().clear();
            } else {
                Toast.makeText(DisplayingTemplatesActivity.this,
                        "That name is already taken", Toast.LENGTH_LONG).show();
            }
        });

        logoutButton.setOnClickListener(view -> {
            if (userInteractFacade.logout()) {
                Intent intent = new Intent(DisplayingTemplatesActivity.this, MainActivity.class);
                startActivity(intent);
            } else{
                Toast.makeText(DisplayingTemplatesActivity.this, "Logout unsuccessful",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
