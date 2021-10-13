/*
 * This file defines a GroceryItem class.
 */
package com.cactus.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroceryItem implements Entity {

    private String name;
    private ArrayList<Integer> userIdList;
    private long groceryListId;
    private Map<Integer, Integer> userMentions;

    private long id;


    /**
     * Creates a new GroceryItem with the given name and id number and
     * capacity.
     *
     * @param name A String containing the User's name.
     */
    public GroceryItem(String name, long groceryListId) {
        this.name = name;
        this.userIdList = new ArrayList<Integer>();
        this.groceryListId = groceryListId;
        this.userMentions = new HashMap<Integer, Integer>();

        this.id = 0L;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getUserIdList() {
        return userIdList;
    }

    public long getGroceryListId() {
        return groceryListId;
    }

    public Map<Integer, Integer> getUserMentions() {
        return userMentions;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addUserId(int userId) {
        userIdList.add(userId);
    }

}
