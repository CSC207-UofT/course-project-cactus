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
        EditText name = findViewById(R.id.currentName);
        EditText password = findViewById(R.id.changedPassword);
        Button logoutButton = findViewById(R.id.logoutButton);
        Button addFriendButton = findViewById(R.id.addFriendButton);
        Button changeButton = findViewById(R.id.changeButton);
        name.setHint(userInteractFacade.getName());
        //TODO: Add a getName method to UserInteractFacade that returns the user's name.
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

        changeButton.setOnClickListener(view -> {
            String newName = name.getText().toString();
            String newPassword = password.getText().toString();
            if ((!newName.equals("")) & (!newPassword.equals(""))) {
            //TODO: create a changeName and changePassword methods in the userInteractFacade
                userInteractFacade.changeName(newName);
                userInteractFacade.changePassword(newPassword);
            } else if (!newName.equals("")){
                userInteractFacade.changeName(newName);
            } else if (!newPassword.equals("")) {
                userInteractFacade.changePassword(newPassword);
            }else{
                Toast.makeText(UserProfileActivity.this, "Enter new username or password", Toast.LENGTH_LONG).show();
            }
        });

    }
}