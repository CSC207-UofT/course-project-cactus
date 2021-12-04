package com.cactus.ui;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;

public class AddFriendActivity extends AppCompatActivity {

    @Inject
    UserInteractFacade userInteractFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        setTitle("Add Friend");

        displayOptions();
    }

    private void displayOptions(){
        EditText friendUsername = findViewById(R.id.friendUsername);
        Button addFriendButton = findViewById(R.id.addButton);

        addFriendButton.setOnClickListener(view -> {
            //TODO: Add "addFriend" method to UserInteractFacade so you will be able to handle this case.
        });

    }

}