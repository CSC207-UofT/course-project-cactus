package com.cactus.ui;


import android.widget.*;
import java.util.ArrayList;

/***
 * Represents the activity responsible for adding and displaying friends
 */

public class AddFriendActivity extends AbstractActivity {

    private ArrayList<String> friends;
    private ListView listView;

    @Override
    protected AbstractActivity activity(){
        return this;
    }

    @Override
    protected void activitySetup(){
        setContentView(R.layout.activity_add_friend);

        setTitle(this.userInteractFacade.getUserName());

        //TODO: Add a getFriendList to UserInteractFacade
        friends = userInteractFacade.getFriendsList();
        CustomFriendListAdapter customFriendListAdapter = new CustomFriendListAdapter(this, R.layout.friend_list_layout, friends, ((CereusApplication) getApplicationContext()).appComponent);
        listView = findViewById(R.id.friendViewDisplayItem);
        listView.setAdapter(customFriendListAdapter);
    }

    @Override
    protected void displayOptions(){
        EditText friendUsername = findViewById(R.id.friendUsername);
        Button addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(view -> {
            String givenUsername = friendUsername.getText().toString();

            //TODO: Add addFriend method to UserInteractFacade
            if (!friends.contains(givenUsername) & this.userInteractFacade.addFriend(givenUsername)) {
                friends.add(givenUsername);
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                friendUsername.getText().clear();
            } else if (!this.userInteractFacade.addFriend(givenUsername)) {
                Toast.makeText(AddFriendActivity.this, "Username not found", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AddFriendActivity.this, "That friend is already added", Toast.LENGTH_LONG).show();
            }
        });
    }

}
