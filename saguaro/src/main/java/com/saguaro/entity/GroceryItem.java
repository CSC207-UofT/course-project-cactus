package com.saguaro.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * This class implements a grocery item entity, and is used by Hibernate to generate a
 * corresponding database representation.
 * <p>
 * Grocery items are contained within grocery lists, however grocery items are shared in
 * between lists. This means that two lists containing an item with the same name will
 * have pointers to the same item object.
 *
 * @author Charles Wong
 */
@Entity
public class GroceryItem {

    /**
     * The name of this grocery item. Cannot be blank.
     */
    @NotBlank
    @Id
    private String name;

    /**
     * The grocery lists that this item is a part of.
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "items")
    private Collection<GroceryList> lists;

    /**
     * Creates a new GroceryItem with an uninitialized name that does not belong to
     * any grocery lists.
     **/
    public GroceryItem() {
        this.lists = new HashSet<>();
    }

    /**
     * Creates a new GroceryItem with the given name, that does not belong to any grocery
     * lists.
     * <p>
     * Required by Jackson for serialization/deserialization of grocery lists.
     *
     * @param name the String name of this object
     */
    public GroceryItem(String name) {
        this.name = name;
        this.lists = new HashSet<>();
    }

    /**
     * Get the name of this grocery item.
     *
     * @return the name of this grocery item
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this grocery item.
     *
     * @param name the String name to set this item to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get all the grocery lists that contain this item
     *
     * @return a Collection of GroceryLists that contain this item
     */
    public Collection<GroceryList> getLists() {
        return lists;
    }

    /**
     * Add a grocery list to the collection of lists that contain this item. Must be
     * called when adding a GroceryItem to a GroceryList to maintain the bi-directional
     * relationship.
     *
     * @param list a GroceryList to add
     */
    void addList(GroceryList list) {
        this.lists.add(list);
    }

    /**
     * Remove a grocery list from the collection of lists that contain this item. Must
     * be called when removing a GroceryItem from a GroceryList to maintain the
     * bi-directional relationship.
     *
     * @param list a GroceryList to remove
     */
    void removeList(GroceryList list) {
        this.lists.remove(list);
    }

    /**
     * Compares an object against this grocery item. The object being compared is equal
     * to this GroceryItem if and only if it is a GroceryItem with the same name.
     *
     * @param o an Object to compare against
     * @return true if the object being compared is equal to this object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroceryItem that = (GroceryItem) o;
        return name.equals(that.name);
    }

    /**
     * Generates a hash for this object. Can be used to compare HashSets of GroceryItems.
     *
     * @return a hash for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
