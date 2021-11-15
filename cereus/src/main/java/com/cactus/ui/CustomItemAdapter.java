package com.cactus.ui;

import android.content.Context;

import java.util.List;

public class CustomItemAdapter extends CustomAdapter {

    public CustomItemAdapter(Context context, int resource, List<String> objects, ApplicationComponent applicationComponent) {
        super(context, resource, objects, applicationComponent);
    }

    void buttonClickAction(int position){
        this.objects.remove(this.objects.get(position));
        this.notifyDataSetChanged();
    }

    void viewClickAction(int position){
        // Do nothing
    }
}
