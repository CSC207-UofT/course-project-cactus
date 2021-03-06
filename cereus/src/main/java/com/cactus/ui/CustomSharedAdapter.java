package com.cactus.ui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;

import java.util.List;

public class CustomSharedAdapter extends CustomAdapter {

    private final static String LOG_TAG = "CustomSharedAdapter";

    /***
     * Initializes a new CustomSharedAdapter
     *
     * @param context current context
     * @param resource variable for the layout you wish to use
     * @param objects the list object
     * @param applicationComponent the applicationComponent variable
     */
    public CustomSharedAdapter(Context context, int resource, List<String> objects, ApplicationComponent applicationComponent) {
        super(context, resource, objects, applicationComponent);
    }

    /***
     * Actions for custom shared adapter
     *
     * @param view the current android view
     * @param position index of the chosen element
     */
    @Override
    void ViewAction(View view, int position) {
        final TextView text = view.findViewById(R.id.shared_entry);
        text.setText(getItem(position));

        final Button button;
        button = view.findViewById(R.id.unshare_button);

        button.setOnClickListener(thisView -> {
            try {
                this.userInteractFacade.unshareList(this.objects.get(position));

                this.objects.remove(this.objects.get(position));
                this.notifyDataSetChanged();
            } catch (InvalidParamException | ServerException e) {
                Log.d(CustomSharedAdapter.LOG_TAG, e.getMessage());
                Toast.makeText(this.context, e.getToastMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
