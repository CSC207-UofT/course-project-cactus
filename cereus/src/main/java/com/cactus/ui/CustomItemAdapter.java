package com.cactus.ui;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

public class CustomItemAdapter extends CustomAdapter {

    public CustomItemAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    void buttonClickAction(int position){
//        Toast.makeText(getContext(), "Delete item at " + position + " called " + this.objects.get(position), Toast.LENGTH_SHORT).show();
        this.objects.remove(this.objects.get(position));
        this.notifyDataSetChanged();
    }

    void viewClickAction(int position){
//        Toast.makeText(getContext(), "Select item at " + position + " called " + this.objects.get(position), Toast.LENGTH_SHORT).show();
        // Do nothing
    }
}
