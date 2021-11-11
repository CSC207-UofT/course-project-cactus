package com.cactus.ui;

import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;
import java.util.ArrayList;

public class DisplayingListsActivity extends AppCompatActivity {

    @Inject
    UserInteractFacade userInteractFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateListView();
    }

    private void populateListView(){
        ArrayList<String> items = userInteractFacade.getGroceryListNames();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_view, items);

        ListView list = (ListView) findViewById(R.id.listViewDisplayList);
        list.setAdapter(adapter);
    }

}
