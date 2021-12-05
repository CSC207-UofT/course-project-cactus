package com.cactus.ui;

import android.util.Log;
import android.widget.*;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;

import java.util.List;

public class AddFriendActivity extends AbstractActivity {

    private final static String LOG_TAG = "AddFriendActivity";

    private List<String> friends;
    private ListView listView;

    @Override
    protected AbstractActivity activity(){
        return this;
    }

    @Override
    protected void activitySetup(){
        setContentView(R.layout.activity_add_friend);
        setTitle("Add Friend");

        friends = this.userInteractFacade.getFriends();
        CustomFriendAdapter customFriendAdapter = new CustomFriendAdapter(this, R.layout.friend_layout, friends, ((CereusApplication) getApplicationContext()).appComponent);
        listView = findViewById(R.id.itemViewDisplayFriend);
        listView.setAdapter(customFriendAdapter);
    }

    @Override
    protected void displayOptions(){
        EditText friendUsername = findViewById(R.id.friendUsername);
        Button addFriendButton = findViewById(R.id.addFriendButton);

        addFriendButton.setOnClickListener(view -> {
            String username = friendUsername.getText().toString();

            try {
                this.userInteractFacade.addFriend(username);

                if (!friends.contains(username)) {
                    friends.add(username);
                    ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                    friendUsername.getText().clear();
                } else {
                    throw new InvalidParamException("This user is already your friend", "Attempted to add friend: " + username);
                }

            } catch (InvalidParamException | ServerException e) {
                Log.d(AddFriendActivity.LOG_TAG, e.getMessage());
                Toast.makeText(AddFriendActivity.this, e.getToastMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}