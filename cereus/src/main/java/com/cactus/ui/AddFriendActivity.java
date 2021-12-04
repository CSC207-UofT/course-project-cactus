package com.cactus.ui;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;

public class AddFriendActivity extends AbstractActivity {

    @Override
    protected AbstractActivity activity(){
        return this;
    }

    @Override
    protected void activitySetup(){
        setContentView(R.layout.activity_add_friend);
        setTitle("Add Friend");
    }

    @Override
    protected void displayOptions(){
        EditText friendUsername = findViewById(R.id.friendUsername);
        Button addFriendButton = findViewById(R.id.addFriendButton);

        addFriendButton.setOnClickListener(view -> {
            //TODO: Add "addFriend" method to UserInteractFacade so you will be able to handle this case.
        });

    }

}