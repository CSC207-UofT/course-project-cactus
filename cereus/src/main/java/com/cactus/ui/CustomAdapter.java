package com.cactus.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;
import java.util.List;

/***
 * Super class for CustomListAdapter and CustomItemAdapter
 * A custom version of ArrayAdapter for displaying list elements with text and a button
 */
public abstract class CustomAdapter extends ArrayAdapter<String> {

    protected int resource;
    protected List<String> objects;
    protected final LayoutInflater mInflater;
    protected Context context;

    @Inject
    UserInteractFacade userInteractFacade;

    /***
     * Initializes a new CustomAdapter
     *
     * @param context current context
     * @param resource variable for the layout you wish to use
     * @param objects the list object
     * @param applicationComponent the applicationComponent variable
     */
    public CustomAdapter(Context context, int resource, List<String> objects, ApplicationComponent applicationComponent) {
        super(context, resource, objects);
        applicationComponent.inject(this);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.mInflater = LayoutInflater.from(context);
    }

    /***
     * Overridden version of getView from ArrayAdapter
     *
     * get the current element in the listView list
     *
     * @param position index of this element
     * @param convertView view variable
     * @param parent view parent for xml use
     * @return the view that is generated
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final View view;
        final TextView text;
        final Button button;

        if(convertView == null){
            view = this.mInflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        text = view.findViewById(R.id.text);
        text.setText(getItem(position));

        button = view.findViewById(R.id.deleteButton);

        button.setOnClickListener(thisView -> buttonClickAction(position));

        view.setOnClickListener(thisView -> viewClickAction(position));

        return view;
    }

    /***
     * Action that is called when the button on a specific element is pressed
     *
     * @param position index of the chosen element
     */
    abstract void buttonClickAction(int position);

    /***
     * Action that is called when the specific element is pressed (the entire element)
     *
     * @param position index of the chosen element
     */
    abstract void viewClickAction(int position);
}