package com.cactus.managers;

import com.cactus.entities.GroceryItem;
import com.cactus.entities.GroceryList;

public class GroceryListManager {

    public GroceryListManager() {}

    public GroceryList createGroceryList(String name, long userId) {
        GroceryList gList = new GroceryList(name);
        gList.addUserId(userId);

        return gList;
    }

    public GroceryItem addItemToGroceryList(String name, long id) {
        return new GroceryItem(name, id);
    }
}
