package com.cactus.adapters;

import com.cactus.entities.GroceryItem;
import com.cactus.entities.GroceryList;

import javax.inject.Inject;
import java.util.List;

public class WebGroceryAdapter implements GroceryAdapter {

    @Inject
    public WebGroceryAdapter() {}

    @Override
    public List<GroceryList> getGroceryListsByUser(String token) {
        return null;
    }

    @Override
    public GroceryList getGroceryList(long listID, String token) {
        return null;
    }

    @Override
    public List<GroceryItem> getGroceryItems(long listID, String token) {
        return null;
    }

    @Override
    public GroceryList createGroceryList(String nameList, String token) {
        return null;
    }

    @Override
    public boolean setGroceryItems(List<String> items, long listID, String token) {
        return false;
    }

    @Override
    public boolean deleteGroceryList(long listID, String token) {
        return false;
    }
}
