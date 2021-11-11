package com.cactus.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;

public class CreatingGroceryListActivity extends AppCompatActivity {

    private EditText groceryListName;
    private Button addListButton;
    @Inject
    UserInteractFacade userFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((CereusApplication) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_grocery_list);

        groceryListName = findViewById(R.id.groceryListName);
        addListButton = findViewById(R.id.addListNameButton);


//        addListButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String givenGroceryListName = groceryListName.getText().toString();
//                userFacade.newGroceryList(givenGroceryListName);
//                Intent intent = new Intent(CreatingGroceryListActivity.this, AddItemsActivity.class);
//                startActivity(intent);
//
//            }
//        });
    }
}