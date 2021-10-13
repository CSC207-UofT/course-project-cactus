/*
 * This file defines a GroceryList class.
 */
package com.cactus;

import java.util.ArrayList;

public class GroceryList {

    private String name;
    private int id;
    private ArrayList<Integer> userIdList;


    /**
     * Creates a new GroceryList with the given name.
     *
     * @param name A String containing the User's name.
     */
    public GroceryList(String name){
        this.name = name;
        this.id = (int) Math.floor(Math.random()*(99999999-10000000+1)+10000000);
        this.userIdList = new ArrayList<Integer>();
    }

    public String getName(){
        return name;
    }
    public ArrayList<Integer> getUserIdList(){
        return userIdList;
    }
    public int getId(){
        return id;
    }


    public void setName(String name){
        this.name = name;
    }

    public void setId(int id){
        this.id = id;
    }

    public void addUserId(int userId){
        userIdList.add(userId);
    }
}
