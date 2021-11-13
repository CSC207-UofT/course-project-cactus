package com.cactus.ui;

import android.content.Intent;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cactus.systems.UserInteractFacade;
import javax.inject.Inject;
import java.util.ArrayList;

public class DisplayingListsActivity extends AppCompatActivity {

    private EditText listName;
    private Button addListButton;
    private Button logoutButton;

    @Inject
    UserInteractFacade userInteractFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        ((CereusApplication) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaying_lists);

        listName = findViewById(R.id.listName);
        addListButton = findViewById(R.id.addListButton);
        logoutButton = findViewById(R.id.logoutButtonList);

        ArrayList<String> lists = userInteractFacade.getGroceryListNames();
        CustomListAdapter customListAdapter = new CustomListAdapter(this, R.layout.list_layout, lists);
        ListView listView = (ListView) findViewById(R.id.listViewDisplayList);
        listView.setAdapter(customListAdapter);

        addListButton.setOnClickListener(view -> {
            String givenListName = listName.getText().toString();

            lists.add(givenListName);
            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
        });

        logoutButton.setOnClickListener(view ->{
            if (userInteractFacade.logout()) {
                Intent intent = new Intent(DisplayingListsActivity.this, MainActivity.class);
                startActivity(intent);
            } else{
                Toast.makeText(DisplayingListsActivity.this, "Try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

}
