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

public abstract class CustomAdapter extends ArrayAdapter<String> {

    protected int resource;
    protected List<String> objects;
    protected final LayoutInflater mInflater;
    protected Context context;

    @Inject
    UserInteractFacade userInteractFacade;

    public CustomAdapter(Context context, int resource, List<String> objects, ApplicationComponent applicationComponent) {
        super(context, resource, objects);
        applicationComponent.inject(this);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.mInflater = LayoutInflater.from(context);
    }

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

    abstract void buttonClickAction(int position);

    abstract void viewClickAction(int position);
}