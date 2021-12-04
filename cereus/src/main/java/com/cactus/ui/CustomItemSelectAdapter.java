package com.cactus.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/***
 * List implementation of abstract super class CustomAdapter
 */
public class CustomItemSelectAdapter extends CustomAdapter {

    public RadioButton currentRadioButton = null;
    private String currentItemString;

    /***
     * Initializes a new CustomItemSelectAdapter
     *
     * @param context current context
     * @param resource variable for the layout you wish to use
     * @param objects the list object
     * @param applicationComponent the applicationComponent variable
     */
    public CustomItemSelectAdapter(Context context, int resource, List<String> objects, ApplicationComponent applicationComponent) {
        super(context, resource, objects, applicationComponent);
        this.objects.add(0, "Default");
    }

    /***
     * Actions for custom list adapter
     *
     * @param view the current android view
     * @param position index of the chosen element
     */
    void ViewAction(View view, int position) {
        final RadioButton radioButton;

        radioButton = view.findViewById(R.id.radioButton);
        radioButton.setText(getItem(position));

        radioButton.setOnClickListener(thisView -> {
            if (currentRadioButton != null) {
                currentRadioButton.setChecked(false);
                this.currentItemString = null;
            }

            radioButton.setChecked(true);

            if (position == 0){
                this.currentItemString = null;
            } else {
                this.currentItemString = this.getItem(position);
            }

            currentRadioButton = radioButton;
        });
    }

    /**
     * Returns the currently selected String in this adapter, or null
     * if none are selected
     *
     * @return the currently selected String
     */
    public String getSelectedString() {
        return this.currentItemString;
    }
}
