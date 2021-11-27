/*
 * This file defines a GroceryList class.
 */
package com.saguaro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Grocery List Entity
 */
@Entity
public class GroceryList {

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "LIST_ID")
    private long id;

    private String name;

    @NotNull
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
    private User owner;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "SHARED_LISTS",
            joinColumns = @JoinColumn(
                    name = "LIST_ID",
                    referencedColumnName = "LIST_ID"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "USER_ID",
                    referencedColumnName = "USER_ID"
            )
    )
    private List<User> shared;

    /**
     * Creates a new GroceryList.
     */
    public GroceryList() {
        this.items = new ArrayList<>();
        this.shared = new ArrayList<>();
    }

    /**
     * Constructor for Jackson deserialization, specifying that ID and items are required
     */
    public GroceryList(@JsonProperty(value = "id", required = true) long id,
                       @JsonProperty(value = "items", required = true) List<GroceryItem> items) {
        this.id = id;
        this.items = items;
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

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User owner) {
        if (this.owner == null) {
            this.owner = owner;
            this.owner.addGroceryList(this);
        }
    }

    public List<User> getShared() {
        return this.shared;
    }

    public void addSharedUser(User user) {
        if (user != this.owner && !this.shared.contains(user)) {
            this.shared.add(user);
             user.addSharedList(this);
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
        this.owner.removeGroceryList(this);

        for (GroceryItem item : items) {
            item.removeList(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroceryList that = (GroceryList) o;
        return id == that.id && items.equals(that.items) && Objects.equals(name, that.name) && Objects.equals(owner, that.owner);
    }
}
