package com.cactus.ui;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;
import java.util.List;

public class CustomListAdapter extends CustomAdapter {


    public CustomListAdapter(Context context, int resource, List<String> objects, ApplicationComponent applicationComponent) {
        super(context, resource, objects, applicationComponent);
    }

    void buttonClickAction(int position){
        this.userInteractFacade.deleteGroceryList(this.objects.get(position));
        this.objects.remove(this.objects.get(position));
        this.notifyDataSetChanged();
    }

    void viewClickAction(int position){
        this.userInteractFacade.setCurrentGroceryList(this.objects.get(position));
        Intent intent = new Intent(getContext(), DisplayingItemsActivity.class);
        getContext().startActivity(intent);
    }
}
