/*
 * This file defines a GroceryItem class.
 */
package com.saguaro.entity;


import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

/**
 * Grocery Item Entity
 */
@Entity
public class GroceryItem {

    @Id
    private String name;

    @ManyToMany(mappedBy = "items")
    private Collection<GroceryList> list;

    /**
     * Creates a new GroceryItem
     **/
    public GroceryItem() {
        this.list = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<GroceryList> getLists() {
        return list;
    }

    void addList(GroceryList list) {
        this.list.add(list);
    }

    void removeList(GroceryList list) {
        this.list.remove(list);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroceryItem that = (GroceryItem) o;
        return name.equals(that.name);
    }
}
