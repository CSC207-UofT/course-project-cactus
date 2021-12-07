/*
 * This file defines a GroceryItem class.
 */
package com.cactus.entities;

/**
 * Grocery Item Entity
 */
public class GroceryItem {

    private String name;

    /**
     * Creates a new GroceryItem with the given name and id number and
     * capacity.
     *
     * @param name A String containing the grocery's name.
     */
    public GroceryItem(String name) {
        this.name = name;
    }

    public GroceryItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
