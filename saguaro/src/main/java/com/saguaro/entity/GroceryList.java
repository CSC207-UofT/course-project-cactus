/*
 * This file defines a GroceryList class.
 */
package com.saguaro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Grocery List Entity
 */
@Entity
public class GroceryList {

    @Id
    @GeneratedValue
    @Column(name = "LIST_ID")
    private long id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "LIST_ITEMS",
            joinColumns = @JoinColumn(
                    name = "LIST_ID",
                    referencedColumnName = "LIST_ID"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "ITEM_NAME",
                    referencedColumnName = "name"
            )
    )
    private List<GroceryItem> items;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "OWNER_ID", referencedColumnName = "USER_ID", nullable = false)
    private User user;

    /**
     * Creates a new GroceryList.
     */
    public GroceryList(){
        this.items = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (this.user == null) {
            this.user = user;
            this.user.addGroceryList(this);
        }
    }

    public List<GroceryItem> getItems() {
        return this.items;
    }

    public void addItem(GroceryItem item) {
        if (!this.items.contains(item)) {
            this.items.add(item);
            item.addList(this);
        }
    }

    public void removeItem(GroceryItem item) {
        if (this.items.remove(item)) {
            item.removeList(this);
        }
    }

    @PreRemove
    void removeList() {
        this.user.removeGroceryList(this);

        for (GroceryItem item: items) {
            item.removeList(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroceryList that = (GroceryList) o;
        return id == that.id && items.equals(that.items) && Objects.equals(name, that.name) && Objects.equals(user, that.user);
    }
}
