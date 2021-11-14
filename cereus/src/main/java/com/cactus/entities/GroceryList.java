/*
 * This file defines a GroceryList class.
 */
package com.cactus.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Grocery List Entity
 */

public class GroceryList {

    private String name;
    private long id;


    /**
     * Creates a new GroceryList with the given name.
     *
     * @param name A String containing the User's name.
     */

    public GroceryList(String name) {
        this.name = name;
        this.id = 0L;
    }

    public GroceryList(){}

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
