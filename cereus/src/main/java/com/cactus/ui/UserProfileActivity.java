package com.cactus.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

public class UserProfileActivity extends AbstractActivity {

    private boolean editing;

    @Override
    AbstractActivity activity() {
        return this;
    }

    @Override
    void activitySetup() {
        setContentView(R.layout.activity_user_profile);

        // hide user button, since we're already on the activity
        Toolbar toolbar = findViewById(R.id.cereusToolBar);
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

            EditText usernameText = this.findViewById(R.id.username_text);
            EditText nameText = findViewById(R.id.name_text);
            EditText passwordText = findViewById(R.id.password_text);

            if (!editing) {
                editing = true;
                clicked.setText("Save");

                this.enableFields(usernameText);

                this.enableFields(nameText);

                this.enableFields(passwordText);
            } else {
                editing = false;
                clicked.setText("Edit");
                // TODO: save edits

                this.disableFields(usernameText);

                this.disableFields(nameText);

                passwordText.setText("");
                this.disableFields(passwordText);
            }
        });

        Button friendsButton = this.findViewById(R.id.friends_button);
        friendsButton.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileActivity.this, AddFriendActivity.class);
            startActivity(intent);
        });

        Button logoutButton = this.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(view -> {
            if (this.userInteractFacade.logout()){
                Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                startActivity(intent);
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