package com.cactus.ui;

import android.app.Activity;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.cactus.systems.UserInteractFacade;
import javax.inject.Inject;
import java.util.ArrayList;

public class DisplayingListsActivity extends AppCompatActivity {

    private EditText listName;
    private Button addListButton;

    @Inject
    UserInteractFacade userInteractFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        ((CereusApplication) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaying_lists);

        listName = findViewById(R.id.listName);
        addListButton = findViewById(R.id.addListButton);

        ArrayList<String> items = userInteractFacade.getGroceryListNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        ListView listView = (ListView) findViewById(R.id.listViewDisplayList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int position, long id) {
                items.remove(items.get(position));
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        });
        listView.setAdapter(adapter);

        addListButton.setOnClickListener(view -> {
            String givenListName = listName.getText().toString();

            items.add(givenListName);
            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
        });
    }

}
