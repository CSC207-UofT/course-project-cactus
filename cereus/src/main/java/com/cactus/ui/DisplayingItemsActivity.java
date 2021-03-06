package com.cactus.ui;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;
import org.w3c.dom.Text;

import java.util.*;

/***
 * Class responsible for the displaying items page
 */
public class DisplayingItemsActivity extends AbstractActivity {

    private final static String LOG_TAG = "DisplayItemsActivity";

    private List<String> items;
    private ListView listView;
    private String listName;
    private String username;
    private String listOwner;
    private List<String> sharedUsers;
    private boolean reverseSort;

    /**
     * Getter for the current activity so the abstract class can use it
     * @return current activity
     */
    @Override
    protected AbstractActivity getActivity() {
        return this;
    }

    /**
     * Set up the activity before displaying
     */
    @Override
    protected void activitySetup() {
        setContentView(R.layout.activity_displaying_items);

        setTitle(this.userInteractFacade.getListName());

        try {
            items = userInteractFacade.getGroceryItemNames();
            listName = userInteractFacade.getListName();
            username = userInteractFacade.getUsername();
            listOwner = userInteractFacade.getGroceryListOwnerUserName();
            sharedUsers = userInteractFacade.getGroceryListSharedUsers();

        } catch (InvalidParamException | ServerException e) {
            Log.d(DisplayingItemsActivity.LOG_TAG, e.getMessage());
            Toast.makeText(DisplayingItemsActivity.this, e.getToastMessage(), Toast.LENGTH_LONG).show();
        }

        CustomItemAdapter customItemAdapter = new CustomItemAdapter(this, R.layout.item_layout, items, ((CereusApplication) getApplicationContext()).appComponent);
        listView = findViewById(R.id.listViewDisplayItem);
        listView.setAdapter(customItemAdapter);

        // Add title above the list
        TextView listNameText = findViewById(R.id.displayListName);
        listNameText.setText(listName);


        // Add whether the User was an Editor or Owner of the List
        TextView shared = findViewById(R.id.displayShared);
        String sharedText;
        if (username.equals(listOwner)) {
            sharedText = "Owner";
        } else {
            sharedText = "Editor";
        }
        shared.setText(sharedText);


    }

    /**
     * Set up for view elements like buttons and text
     */
    @Override
    protected void displayOptions() {
        EditText itemName = findViewById(R.id.itemName);
        Button addItemButton = findViewById(R.id.addItemButton);
        Button shareButton = findViewById(R.id.share_link);
        Button sortButton = findViewById(R.id.sortButton);

        sortButton.setOnClickListener(view -> {

            if (!this.reverseSort) {
                items.sort(Comparator.comparing(String::toString));
                sortButton.setText("Sort Z - A");
                this.reverseSort = true;
            }else{
                items.sort(Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));
                sortButton.setText("Sort A - Z");
                this.reverseSort = false;
            }

            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
        });

        addItemButton.setOnClickListener(view -> {
            String givenItemName = itemName.getText().toString();

            if (!items.contains(givenItemName)) {
                items.add(givenItemName);
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                itemName.getText().clear();
            } else {
                Toast.makeText(DisplayingItemsActivity.this, "That name is already taken", Toast.LENGTH_LONG).show();
            }
        });

        shareButton.setOnClickListener(view -> {
            // inflate popup
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.share_list_layout, null);

            // setup popup title
            TextView title = popupView.findViewById(R.id.share_title);
            String shareTitleTextDisplay = "Share list: " + listName;
            title.setText(shareTitleTextDisplay);

            // setup adapter
            ListView sharedList = popupView.findViewById(R.id.shared_friends);
            CustomSharedAdapter customSharedAdapter = new CustomSharedAdapter(this, R.layout.share_friend_individual_layout, sharedUsers, ((CereusApplication) getApplicationContext()).appComponent);
            sharedList.setAdapter(customSharedAdapter);

            // setup share functionality
            EditText submitInput = popupView.findViewById(R.id.share_input);
            Button submitShare = popupView.findViewById(R.id.submit_share);
            submitShare.setOnClickListener(submitView -> {
                String inputString = submitInput.getText().toString();

                if (!sharedUsers.contains(inputString)) {
                    try {
                        this.userInteractFacade.shareList(inputString);

                        sharedUsers.add(inputString);
                        ((BaseAdapter) sharedList.getAdapter()).notifyDataSetChanged();
                        submitInput.getText().clear();
                    } catch (InvalidParamException | ServerException e) {
                        Log.d(DisplayingItemsActivity.LOG_TAG, e.getMessage());
                        Toast.makeText(DisplayingItemsActivity.this, e.getToastMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(DisplayingItemsActivity.this, "List is already shared with this user", Toast.LENGTH_LONG).show();
                }
            });


            // make popup
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            // dismiss the popup window when touched
            popupView.setOnTouchListener((v, event) -> {
                popupWindow.dismiss();
                return true;
            });
        });
    }

    /**
     * Logic for what to do when this activity is destroyed
     * <p>
     * on delete, the items are saved to the database
     */
    @Override
    protected void onStop() {
        try {
            userInteractFacade.addGroceryItems(items);

        } catch (InvalidParamException | ServerException e) {
            Log.d(DisplayingItemsActivity.LOG_TAG, e.getMessage());
            Toast.makeText(DisplayingItemsActivity.this, e.getToastMessage(), Toast.LENGTH_LONG).show();
        }

        super.onStop();
    }

}
