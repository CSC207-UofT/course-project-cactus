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
    private Boolean isTemplate;

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

    public Boolean getIsTemplate() {
        return isTemplate;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }
}
