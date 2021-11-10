package com.cactus.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class OptionsActivity extends AppCompatActivity {

    private Button newGroceryList;
    private Button groceryLists;
    private Button logoutButton;
    @Inject
    private UserInteractFacade facade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((CereusApplication) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        newGroceryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionsActivity.this, CreatingGroceryListActivity.class);
                startActivity(intent);

            }
        });

        groceryLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionsActivity.this, GroceryLists.class);
                startActivity(intent);

            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionsActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });



    }
}