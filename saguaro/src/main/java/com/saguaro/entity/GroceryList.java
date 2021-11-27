package com.saguaro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class implements a grocery list entity, and is used by Hibernate to generate a
 * corresponding database representation.
 * <p>
 * Grocery lists are associated with one "owner" user, and contain a list of grocery
 * items.
 *
 * @author Charles Wong
 */
@Entity
public class GroceryList {

    /**
     * The id of this grocery list
     */
    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "LIST_ID")
    private long id;

    /**
     * The name of this grocery list
     */
    private String name;

    /**
     * The grocery items in this list
     */
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

    /**
     * The owner of this list
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "OWNER_ID", referencedColumnName = "USER_ID", nullable = false)
    private User user;

    /**
     * Creates a new GroceryList, with null ID, name, and user. The newly created
     * grocery list contains an empty list of items.
     */
    public GroceryList() {
        this.items = new ArrayList<>();
    }

    /**
     * Constructor for Jackson deserialization, specifying that ID and items are required.
     * All other fields are initialized to null if not provided.
     *
     * @param id    a long ID to initalize this list with
     * @param items a List of GroceryItems this list to put in this list
     */
    public GroceryList(@JsonProperty(value = "id", required = true) long id,
                       @JsonProperty(value = "items", required = true) List<GroceryItem> items) {
        this.id = id;
        this.items = items;
    }

    /**
     * Gets the ID of this list
     *
     * @return the ID of this list
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the name of this list
     *
     * @return the name of this list
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this list
     *
     * @param name a String to set this list's name to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the owner of this list
     *
     * @return the User that is the owner of this list
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the owner of this list if one is not set already. Once initially set, the
     * owner of a list cannot be changed.
     *
     * @param user the User to set as the owner of this list
     */
    public void setUser(User user) {
        if (this.user == null) {
            this.user = user;
            this.user.addGroceryList(this);
        }
    }

    /**
     * Get the items in this grocery list
     *
     * @return the List of GroceryItems in this list
     */
    public List<GroceryItem> getItems() {
        return this.items;
    }

    /**
     * Add a GroceryItem to this list if it is not already in the list.
     *
     * @param item the GroceryItem to add to this list
     */
    public void addItem(GroceryItem item) {
        if (!this.items.contains(item)) {
            this.items.add(item);
            item.addList(this);
        }
    }

    /**
     * Remove a GroceryItem from this list
     *
     * @param item the GroceryItem to remove
     */
    public void removeItem(GroceryItem item) {
        if (this.items.remove(item)) {
            item.removeList(this);
        }
    }

    /**
     * Performs all necessary operations to maintain database relations before deleting
     * this list. This list is removed from the collection of lists the owner User owns,
     * and removes all reference to this list from this list's contained items.
     * <p>
     * This method is called automatically by Hibernate on deletion of this list.
     */
    @PreRemove
    void removeList() {
        this.user.removeGroceryList(this);

        for (GroceryItem item : items) {
            item.removeList(this);
        }
    }

    /**
     * Compares an object against this grocery list. The object being compared is equal
     * to this GroceryList if and only if it is a GroceryList with the same ID, items,
     * name, and owner User. The ID and items must be non-null.
     *
     * @param o an Object to compare against
     * @return true if the object being compared is equal to this object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroceryList that = (GroceryList) o;
        return id == that.id && items.equals(that.items) && Objects.equals(name, that.name) && Objects.equals(user, that.user);
    }
}
