package com.cactus.ui;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;
import org.w3c.dom.Text;

import java.util.List;

/***
 * Represents the activity responsible for displaying the grocery items
 */
public class DisplayingItemsActivity extends AbstractActivity {

    private final static String LOG_TAG = "DisplayItemsActivity";

    private List<String> items;
    private ListView listView;

    private List<String> sharedUsers;

    @Override
    protected AbstractActivity activity() {
        return this;
    }

    @Override
    protected void activitySetup() {
        setContentView(R.layout.activity_displaying_items);

        setTitle(this.userInteractFacade.getListName());


        try {
            items = userInteractFacade.getGroceryItemNames();
            sharedUsers = userInteractFacade.getGroceryListSharedUsers();

        } catch (InvalidParamException | ServerException e) {
            Log.d(DisplayingItemsActivity.LOG_TAG, e.getMessage());
            Toast.makeText(DisplayingItemsActivity.this, e.getToastMessage(), Toast.LENGTH_LONG).show();
        }

        CustomItemAdapter customItemAdapter = new CustomItemAdapter(this, R.layout.item_layout, items, ((CereusApplication) getApplicationContext()).appComponent);
        listView = findViewById(R.id.listViewDisplayItem);
        listView.setAdapter(customItemAdapter);
    }

    /***
     * Display the add item text field and button along with logout button
     */
    @Override
    protected void displayOptions() {
        EditText itemName = findViewById(R.id.itemName);
        Button addItemButton = findViewById(R.id.addItemButton);
        Button shareButton = findViewById(R.id.share_link);

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

        shareButton.setOnClickListener(view ->{
            // inflate popup
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.share_list_layout, null);

            // setup popup title
            TextView title = popupView.findViewById(R.id.share_title);
            title.setText("Share list: " + this.userInteractFacade.getListName());

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

                    } catch (InvalidParamException | ServerException e) {
                        Log.d(DisplayingItemsActivity.LOG_TAG, e.getMessage());
                        Toast.makeText(DisplayingItemsActivity.this, e.getToastMessage(), Toast.LENGTH_LONG).show();
                    }

                    items.add(inputString);
                    ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                    submitInput.getText().clear();
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
        });
    }

    /**
     * Logic for what to do when this activity is destroyed
     * <p>
     * on delete, the items are saved to the database
     */
    @Override
    protected void onDestroy() {
        try {
            userInteractFacade.addGroceryItems(items);

        } catch (InvalidParamException | ServerException e) {
            Log.d(DisplayingItemsActivity.LOG_TAG, e.getMessage());
            Toast.makeText(DisplayingItemsActivity.this, e.getToastMessage(), Toast.LENGTH_LONG).show();
        }

        super.onDestroy();
    }

}
