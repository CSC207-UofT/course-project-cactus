package com.cactus.ui;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;

/***
 * List implementation of abstract super class CustomAdapter
 */
public class CustomListAdapter extends CustomAdapter {

    /***
     * Initializes a new CustomListAdapter
     *
     * @param context current context
     * @param resource variable for the layout you wish to use
     * @param objects the list object
     * @param applicationComponent the applicationComponent variable
     */
    public CustomListAdapter(Context context, int resource, List<String> objects, ApplicationComponent applicationComponent) {
        super(context, resource, objects, applicationComponent);
    }

    /**
     * Action that is called when the button on a specific element is pressed
     * Removed the list that was pressed
     *
     * @param position index of the chosen element
     */
    void buttonClickAction(int position) {
        if (this.userInteractFacade.deleteGroceryList(this.objects.get(position))) {
            this.objects.remove(this.objects.get(position));
            this.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), "Failed to delete list", Toast.LENGTH_LONG).show();
        }
    }

    /***
     * Action that is called when the specific element is pressed (the entire element)
     * Enter the list that was pressed and display all the items in that list
     *
     * @param position index of the chosen element
     */
    void viewClickAction(int position) {
        this.userInteractFacade.setCurrentGroceryList(this.objects.get(position));
        Intent intent = new Intent(getContext(), DisplayingItemsActivity.class);
        getContext().startActivity(intent);
    }
}
