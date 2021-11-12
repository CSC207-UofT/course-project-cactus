package com.cactus.ui;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;

public class CustomListAdapter extends CustomAdapter {

    public CustomListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    void buttonClickAction(int position){
        Toast.makeText(getContext(), "Delete list at " + position + " called " + this.objects.get(position), Toast.LENGTH_SHORT).show();
    }

    void viewClickAction(int position){
        Toast.makeText(getContext(), "Select list at " + position + " called " + this.objects.get(position), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), DisplayingItemsActivity.class);
        getContext().startActivity(intent);
    }
}
