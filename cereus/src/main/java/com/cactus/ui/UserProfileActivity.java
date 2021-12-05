package com.cactus.ui;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;

public class UserProfileActivity extends AbstractActivity {

    private final static String LOG_TAG = "UserProfileActivity";
    private boolean editing;

    @Override
    AbstractActivity activity() {
        return this;
    }

    @Override
    void activitySetup() {
        setContentView(R.layout.activity_user_profile);

        // hide user button, since we're already on the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        Button userButton = toolbar.findViewById(R.id.user_button);
        userButton.setVisibility(View.INVISIBLE);

        // set username and name fields to what they are
        EditText usernameText = findViewById(R.id.username_text);
        usernameText.setText(this.userInteractFacade.getUsername());
        this.disableFields(usernameText);

        EditText nameText = findViewById(R.id.name_text);
        nameText.setText(this.userInteractFacade.getName());
        this.disableFields(nameText);

        EditText passwordText = findViewById(R.id.password_text);
        this.disableFields(passwordText);
    }

    /**
     * Set all button listeners
     */
    protected void displayOptions() {

        Button editButton = this.findViewById(R.id.edit_user_button);
        editButton.setOnClickListener(view -> {
            Button clicked = (Button) view;

            EditText nameText = findViewById(R.id.name_text);
            EditText passwordText = findViewById(R.id.password_text);

            if (!editing) {
                editing = true;
                clicked.setText("Save");

                this.enableFields(nameText);

                this.enableFields(passwordText);
            } else {
                try {
                    this.userInteractFacade.editUserDetails(nameText.getText().toString(), passwordText.getText().toString());

                    editing = false;
                    clicked.setText("Edit");

                    this.disableFields(nameText);

                    passwordText.setText("");
                    this.disableFields(passwordText);

                } catch (InvalidParamException | ServerException e) {
                    Log.d(UserProfileActivity.LOG_TAG, e.getMessage());
                    Toast.makeText(UserProfileActivity.this, e.getToastMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        Button friendsButton = this.findViewById(R.id.friends_button);
        friendsButton.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileActivity.this, AddFriendActivity.class);
            startActivity(intent);
        });

        Button logoutButton = this.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(view -> {
            try {
                this.userInteractFacade.logout();

                Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                startActivity(intent);
            } catch (InvalidParamException | ServerException e) {
                Log.d(UserProfileActivity.LOG_TAG, e.getMessage());
                Toast.makeText(UserProfileActivity.this, e.getToastMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void enableFields(EditText view) {
        view.setEnabled(true);
        view.setFocusableInTouchMode(true);
    }

    private void disableFields(EditText view) {
        view.setEnabled(false);
        view.setFocusable(false);
    }
}