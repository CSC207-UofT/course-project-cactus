package com.cactus.adapters;

import com.cactus.entities.GroceryItem;
import com.cactus.entities.GroceryList;

import javax.inject.Inject;
import java.util.List;

public class WebGroceryAdapter implements GroceryAdapter {

    @Inject
    public WebGroceryAdapter() {}

    @Override
    public List<GroceryList> getGroceryListsByUser(long userid) {
        return null;
    }

    @Override
    public GroceryList getGroceryList(long listid, long userid) {
        return null;
    }

    @Override
    public List<GroceryItem> getGroceryItems(long listid, long userid) {
        return null;
    }

    @Override
    public GroceryList createGroceryList(String nameList, long userid) {
        return null;
    }

    @Override
    public boolean setGroceryItems(List<String> items, long listid, long userid) {
        return false;
    }

    @Override
    public boolean deleteGroceryList(long listid, long userid) {
        return false;
    }
}
