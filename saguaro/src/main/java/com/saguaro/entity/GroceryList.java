/*
 * This file defines a GroceryList class.
 */
package com.saguaro.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "LIST_ITEMS",
            joinColumns = @JoinColumn(
                    name = "LIST_ID",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "ITEM_NAME",
                    referencedColumnName = "name"
            )
    )
    private Collection<GroceryItem> items;

    @ManyToOne
    @JoinColumn(name = "OWNER_ID", referencedColumnName = "USER_ID", nullable = false)
    private User user;

    /**
     * Creates a new GroceryList.
     */
    public GroceryList(){
        this.items = new HashSet<>();
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
        this.user = user;
        user.addGroceryList(this);
    }

    public void addItem(GroceryItem item) {
        this.items.add(item);
        item.addList(this);
    }

    public void removeItem(GroceryItem item) {
        this.items.remove(item);
        item.removeList(this);
    }
}
