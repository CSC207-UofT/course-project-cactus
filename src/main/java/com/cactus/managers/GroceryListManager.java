package com.cactus.managers;

import com.cactus.entities.GroceryItem;
import com.cactus.entities.GroceryList;

public class GroceryListManager {

    GroceryListManager() {}

    public GroceryList createGroceryList(String name) {
        return new GroceryList(name);
    }

    public GroceryItem addItemToGroceryList(String name, long id) {
        return new GroceryItem(name, id);
    }
}
