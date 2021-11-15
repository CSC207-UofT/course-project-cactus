package com.cactus.ui;

import android.content.Intent;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cactus.systems.UserInteractFacade;
import javax.inject.Inject;

public class DisplayingListsActivity extends AppCompatActivity {

    private EditText listName;

    @Inject
    UserInteractFacade userInteractFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        ((CereusApplication) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaying_lists);

        setTitle("Cereus App : " + this.userInteractFacade.getUserName());

        listName = findViewById(R.id.listName);
        Button addListButton = findViewById(R.id.addListButton);
        Button logoutButton = findViewById(R.id.logoutButtonList);

        CustomListAdapter customListAdapter = new CustomListAdapter(this, R.layout.list_layout,
                this.userInteractFacade.getGroceryListNames(), ((CereusApplication) getApplicationContext()).appComponent);
        ListView listView = findViewById(R.id.listViewDisplayList);
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
                Toast.makeText(DisplayingListsActivity.this, "Try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

}
