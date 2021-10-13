/*
 * This file defines a GroceryItem class.
 */
package com.cactus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroceryItem {

    private String name;
    private ArrayList<Integer> userIdList;
    private ArrayList<Integer> groceryIdList;
    private Map<Integer, Integer> userMentions;


    /**
     * Creates a new GroceryItem with the given name and id number and
     * capacity.
     *
     * @param name A String containing the User's name.
     */
    public GroceryItem(String name) {
        this.name = name;
        this.userIdList = new ArrayList<Integer>();
        this.groceryIdList = new ArrayList<Integer>();
        this.userMentions = new HashMap<Integer, Integer>();

    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getUserIdList() {
        return userIdList;
    }

    public ArrayList<Integer> getGroceryIdList() {
        return groceryIdList;
    }

    public Map<Integer, Integer> getUserMentions() {
        return userMentions;
    }




    public void setName(String name) {
        this.name = name;
    }


    public void addUserId(int userId) {
        userIdList.add(userId);

    }

}
