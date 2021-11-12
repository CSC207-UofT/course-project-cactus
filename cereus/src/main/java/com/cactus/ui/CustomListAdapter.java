package com.cactus.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<String> {

    private int resource;
    private List<String> objects;
    private final LayoutInflater mInflater;

    public CustomListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
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

        text = view.findViewById(R.id.listText);
        text.setText(getItem(position));

        button = view.findViewById(R.id.deleteListButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getContext(), "Delete item at " + position, Toast.LENGTH_SHORT).show();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getContext(), "Select item at " + position, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
