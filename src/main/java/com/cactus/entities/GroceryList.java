/*
 * This file defines a GroceryList class.
 */
package com.cactus.entities;

import java.util.ArrayList;

/**
 * Grocery List Entity
 */
public class GroceryList implements Entity {

    private String name;
    private long id;
    private final ArrayList<Long> userIdList;


    /**
     * Creates a new GroceryList with the given name.
     *
     * @param name A String containing the User's name.
     */
    public GroceryList(String name){
        this.name = name;
        this.id = 0L;
        this.userIdList = new ArrayList<>();
    }

    public String getName(){
        return name;
    }

    public ArrayList<Long> getUserIdList(){
        return userIdList;
    }

    public long getId(){
        return id;
    }


    public void setName(String name){
        this.name = name;
    }

    public void setId(long id){
        this.id = id;
    }

    public void addUserId(long userId){
        userIdList.add(userId);
    }
}
