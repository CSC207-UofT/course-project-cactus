/*
 * This file defines a GroceryList class.
 */
package com.cactus.entities;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Grocery List Entity
 */

public class GroceryList {

    private String name;

    private long id;

    private boolean isTemplate;

    private String owner;

    private List<String> sharedUsers;

    private List<GroceryItem> items;

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

    public boolean isTemplate() {
        return isTemplate;
    }

    public void setTemplate(boolean template) {
        isTemplate = template;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getSharedUsers() {
        return sharedUsers;
    }

    public void setSharedUsers(List<String> sharedUsers) {
        this.sharedUsers = sharedUsers;
    }

    public List<String> getItems() {
        return this.items.stream()
                .map(GroceryItem::getName).collect(Collectors.toList());
    }

    public void setItems(List<GroceryItem> items) {
        this.items = items;
    }
}
