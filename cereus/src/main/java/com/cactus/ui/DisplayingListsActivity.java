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

        setTitle("Cereus App : " + this.userInteractFacade.getUserName());

        listName = findViewById(R.id.listName);
        addListButton = findViewById(R.id.addListButton);
        logoutButton = findViewById(R.id.logoutButtonList);

        CustomListAdapter customListAdapter = new CustomListAdapter(this, R.layout.list_layout,
                this.userInteractFacade.getGroceryListNames(), ((CereusApplication) getApplicationContext()).appComponent);
        ListView listView = (ListView) findViewById(R.id.listViewDisplayList);
        listView.setAdapter(customListAdapter);

        addListButton.setOnClickListener(view -> {
            String givenListName = listName.getText().toString();
            if (this.userInteractFacade.newGroceryList(givenListName)){
                customListAdapter.objects.add(givenListName);
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                listName.getText().clear();
            } else {
                Toast.makeText(DisplayingListsActivity.this, "That name is already taken", Toast.LENGTH_LONG).show();
            }
        });

        logoutButton.setOnClickListener(view ->{
            if (userInteractFacade.logout()) {
                Intent intent = new Intent(DisplayingListsActivity.this, MainActivity.class);
                startActivity(intent);
            } else{
                Toast.makeText(DisplayingListsActivity.this, "Logout unsuccessful", Toast.LENGTH_LONG).show();
            }
        });
    }

}
