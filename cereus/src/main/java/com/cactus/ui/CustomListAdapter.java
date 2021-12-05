package com.cactus.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;

import java.util.List;

/***
 * List implementation of abstract super class CustomAdapter
 */
public class CustomListAdapter extends CustomAdapter {

    private final static String LOG_TAG = "CustomListAdapter";

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

    /***
     * Actions for custom list adapter
     *
     * @param view the current android view
     * @param position index of the chosen element
     */
    void ViewAction(View view, int position) {
        final TextView text;
        final Button button;

        text = view.findViewById(R.id.text);
        text.setText(getItem(position));

        button = view.findViewById(R.id.deleteButton);

        button.setOnClickListener(thisView -> {
            try {
                this.userInteractFacade.deleteGroceryList(this.objects.get(position));

            } catch (InvalidParamException | ServerException e) {
                Log.d(CustomListAdapter.LOG_TAG, e.getMessage());
                Toast.makeText(this.context, e.getToastMessage(), Toast.LENGTH_LONG).show();
            }

            this.objects.remove(this.objects.get(position));
            this.notifyDataSetChanged();
        });

        view.setOnClickListener(thisView -> {
            this.userInteractFacade.setCurrentGroceryList(this.objects.get(position));
            Intent intent = new Intent(getContext(), DisplayingItemsActivity.class);
            getContext().startActivity(intent);
        });
    }
}
