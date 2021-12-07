package com.cactus.ui;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import java.util.List;

/***
 * List implementation of abstract super class CustomAdapter
 */
public class CustomItemSelectAdapter extends CustomAdapter {

    public RadioButton currentRadioButton = null;
    private String currentItemString;

    private RadioButton zerothButton;

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
        
        this.objects.add(0, "No template");
    }

    /***
     * Actions for custom item select adapter
     *
     * @param view the current android view
     * @param position index of the chosen element
     */
    void ViewAction(View view, int position) {
        final RadioButton radioButton;

        radioButton = view.findViewById(R.id.radioButton);
        radioButton.setText(getItem(position));

        if (position == 0) {
            zerothButton = radioButton;
            zerothButton.setChecked(true);
        }

        radioButton.setOnClickListener(thisView -> {
            zerothButton.setChecked(false);

            if(currentRadioButton != null) {
                currentRadioButton.setChecked(false);
                this.currentItemString = null;
            }

            radioButton.setChecked(true);
            this.currentItemString = position == 0 ? null : this.getItem(position);

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
