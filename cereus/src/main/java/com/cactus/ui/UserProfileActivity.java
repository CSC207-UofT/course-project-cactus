package com.cactus.ui;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelStore;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;
import java.util.ArrayList;

public class UserProfileActivity extends AbstractActivity {

    @Override
    AbstractActivity activity() {
        return this;
    }

    @Override
    void activitySetup() {
        setContentView(R.layout.activity_user_profile);
//        View view = findViewById(R.id.cereusToolBar);

        Toolbar toolbar = findViewById(R.id.cereusToolBar);
        Button userButton = toolbar.findViewById(R.id.user_button);
        userButton.setVisibility(View.INVISIBLE);
    }

    protected void displayOptions(){
//        EditText name = findViewById(R.id.currentName);
//        EditText password = findViewById(R.id.changedPassword);
//        Button logoutButton = findViewById(R.id.logoutButton);
//        Button addFriendButton = findViewById(R.id.addFriendButton);
//        Button changeButton = findViewById(R.id.changeButton);
////        name.setHint(userInteractFacade.getName());
//        //TODO: Add a getName method to UserInteractFacade that returns the user's name.
//        logoutButton.setOnClickListener(view -> {
//            if (userInteractFacade.logout()) {
//                Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
//                startActivity(intent);
//            } else{
//                Toast.makeText(UserProfileActivity.this, "Logout unsuccessful", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        addFriendButton.setOnClickListener(view -> {
//
//            Intent intent = new Intent(UserProfileActivity.this, AddFriendActivity.class);
//            startActivity(intent);
//
//        });

//        changeButton.setOnClickListener(view -> {
//            String newName = name.getText().toString();
//            String newPassword = password.getText().toString();
//            if ((!newName.equals("")) & (!newPassword.equals(""))) {
//            //TODO: create a changeName and changePassword methods in the userInteractFacade
//                userInteractFacade.changeName(newName);
//                userInteractFacade.changePassword(newPassword);
//            } else if (!newName.equals("")){
//                userInteractFacade.changeName(newName);
//            } else if (!newPassword.equals("")) {
//                userInteractFacade.changePassword(newPassword);
//            }else{
//                Toast.makeText(UserProfileActivity.this, "Enter new username or password", Toast.LENGTH_LONG).show();
//            }
//        });

    }
}