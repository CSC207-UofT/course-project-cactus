package com.cactus.ui;

import android.util.Log;
import android.widget.*;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;

import java.util.List;

/**
 * Class responsible for the add friends page
 */
public class AddFriendActivity extends AbstractActivity {

    private final static String LOG_TAG = "AddFriendActivity";

    private List<String> friends;
    private ListView listView;

    /**
     * Getter for the current activity so the abstract class can use it
     * @return current activity
     */
    @Override
    protected AbstractActivity getActivity(){
        return this;
    }

    /**
     * Set up the activity before displaying
     */
    @Override
    protected void activitySetup(){
        setContentView(R.layout.activity_add_friend);
        setTitle("Add Friend");

        friends = this.userInteractFacade.getFriends();
        CustomFriendAdapter customFriendAdapter = new CustomFriendAdapter(this, R.layout.friend_layout, friends, ((CereusApplication) getApplicationContext()).appComponent);
        listView = findViewById(R.id.itemViewDisplayFriend);
        listView.setAdapter(customFriendAdapter);
    }

    /**
     * Set up for view elements like buttons and text
     */
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