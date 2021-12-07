package com.cactus.ui;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Custom adapter for displaying a list of friends in AddFriendActivity
 */
public class CustomFriendAdapter extends CustomAdapter {

    /***
     * Initializes a new CustomFriendAdapter
     *
     * @param context current context
     * @param resource variable for the layout you wish to use
     * @param objects the list object
     * @param applicationComponent the applicationComponent variable
     */
    public CustomFriendAdapter(Context context, int resource, List<String> objects, ApplicationComponent applicationComponent) {
        super(context, resource, objects, applicationComponent);
    }

    /**
     * Actions for custom friend adapter
     *
     * @param view the current android view
     * @param position index of the chosen element
     */
    @Override
    void ViewAction(View view, int position) {
        final TextView text;

        text = view.findViewById(R.id.text);
        text.setText(getItem(position));
    }
}
