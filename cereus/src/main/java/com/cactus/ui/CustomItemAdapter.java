package com.cactus.ui;

import android.content.Context;
import java.util.List;

/***
 * Item implementation of abstract super class CustomAdapter
 */
public class CustomItemAdapter extends CustomAdapter {

    /***
     * Initializes a new CustomItemAdapter
     *
     * @param context current context
     * @param resource variable for the layout you wish to use
     * @param objects the list object
     * @param applicationComponent the applicationComponent variable
     */
    public CustomItemAdapter(Context context, int resource, List<String> objects, ApplicationComponent applicationComponent) {
        super(context, resource, objects, applicationComponent);
    }

    /***
     * Action that is called when the button on a specific element is pressed
     * Removes the item that was pressed
     *
     * @param position index of the chosen element
     */
    void buttonClickAction(int position){
        this.objects.remove(this.objects.get(position));
        this.notifyDataSetChanged();
    }

    /***
     * Action that is called when the specific element is pressed (the entire element)
     * We do not do anything when items are pressed
     *
     * @param position index of the chosen element
     */
    void viewClickAction(int position){
        // Do nothing
    }
}
