/*
 * This file defines a GroceryList class.
 */
package com.saguaro.entity;

import java.util.ArrayList;

/**
 * Grocery List Entity
 */
public class GroceryList {

    private String name;
    private ArrayList<Long> userIdList;

    /**
     * Creates a new GroceryList with the given name.
     *
     * @param name A String containing the User's name.
     */
    public GroceryList(String name){
        this.name = name;
        this.userIdList = new ArrayList<>();
    }

    public String getName(){
        return name;
    }

    public ArrayList<Long> getUserIdList(){
        return userIdList;
    }

    public void setName(String name){
        this.name = name;
    }

    public void addUserId(long userId){
        userIdList.add(userId);
    }
}
