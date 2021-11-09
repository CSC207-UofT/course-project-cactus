/*
 * This file defines a GroceryItem class.
 */
package com.saguaro.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Grocery Item Entity
 */
@Entity
public class GroceryItem {

    @Id
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "items")
    private Collection<GroceryList> lists;

    /**
     * Creates a new GroceryItem
     **/
    public GroceryItem() {
        this.lists = new HashSet<>();
    }

    public GroceryItem(String name) {
        this.name = name;
        this.lists = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<GroceryList> getLists() {
        return lists;
    }

    void addList(GroceryList list) {
        this.lists.add(list);
    }

    void removeList(GroceryList list) {
        this.lists.remove(list);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroceryItem that = (GroceryItem) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
