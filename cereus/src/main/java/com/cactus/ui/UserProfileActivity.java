package com.cactus.ui;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;

public class UserProfileActivity extends AppCompatActivity {

    @Inject
    UserInteractFacade userInteractFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((CereusApplication) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setTitle(userInteractFacade.getUserName() + "Profile");

        displayOptions();
    }

    private void displayOptions(){
        //TODO: Delete unuseful buttons and convert the three change buttons to one change button.
        EditText name = findViewById(R.id.currentName);
        EditText username = findViewById(R.id.currentUsername);
        EditText newPassword = findViewById(R.id.changedPassword);
        Button logoutButton = findViewById(R.id.logoutButton);
        Button groceryListsButton = findViewById(R.id.groceryListsButton);
        Button addFriendButton = findViewById(R.id.addFriendButton);
        Button changePasswordButton = findViewById(R.id.changePasswordButton);
        Button changeUsernameButton = findViewById(R.id.changeUsernameButton);
        Button changeNameButton = findViewById(R.id.changeNameButton);
        username.setHint(userInteractFacade.getUserName());
        //TODO: Add a getName method to UserInteractFacade that returns the user's name.
        //name.setHint(userInteractFacade.getName());
        logoutButton.setOnClickListener(view -> {
            if (userInteractFacade.logout()) {
                Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                startActivity(intent);
            } else{
                Toast.makeText(UserProfileActivity.this, "Logout unsuccessful", Toast.LENGTH_LONG).show();
            }
        });

        addFriendButton.setOnClickListener(view -> {

            Intent intent = new Intent(UserProfileActivity.this, AddFriendActivity.class);
            startActivity(intent);

        });

        groceryListsButton.setOnClickListener(view -> {

            Intent intent = new Intent(UserProfileActivity.this, DisplayingListsActivity.class);
            startActivity(intent);

        });

        changeNameButton.setOnClickListener(view -> {
            String givenName = name.getText().toString();
            //userInteractFacade.changeName(givenName);
        });

        changeUsernameButton.setOnClickListener(view -> {
            String givenUsername = username.getText().toString();
            //userInteractFacade.changeUsername(givenUsername);
        });

        changePasswordButton.setOnClickListener(view -> {
            String givenNewPassword = newPassword.getText().toString();
            //userInteractFacade.changePassword(givenPassword, newPassword);
        });


    }
}