/*
 * This file defines a GroceryList class.
 */
package com.cactus.entities;

/**
 * Grocery List Entity
 */

public class GroceryList {

    private String name;
    private long id;

    /**
     * Empty constructor for GroceryList, required for Jackson serialization
     */
    public GroceryList() {
    }

    public String getName() {
        return name;
    }


    public long getId() {
        return id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

}
