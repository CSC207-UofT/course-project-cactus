/*
 * This file defines a GroceryList class.
 */
package com.saguaro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    // we ignore the field, but set its getter as the JsonProperty
    // then we can ignore the setter, and have this field only be
    // serialized, but not deserialized
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "OWNER_ID", referencedColumnName = "USER_ID", nullable = false)
    private User owner;

    /**
     * A list of unique Users that this GroceryList is shared with
     */
    @JsonSerialize(using = SharedUserSerializer.class)
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
    private List<User> sharedUsers;

    /**
     * Whether this list is a template
     */
    private boolean isTemplate;

    /**
     * Creates a new GroceryList.
     */
    public GroceryList() {
        this.items = new ArrayList<>();
        this.sharedUsers = new ArrayList<>();
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

    @JsonProperty
    @JsonSerialize(using = OwnerSerializer.class)
    public User getOwner() {
        return this.owner;
    }

    @JsonIgnore
    public void setOwner(User owner) {
        if (this.owner == null) {
            this.owner = owner;
            this.owner.addGroceryList(this);
        }
    }

    /**
     * Get all the users that this list is shared with
     *
     * @return a List of Users that this list is shared with
     */
    public List<User> getSharedUsers() {
        return this.sharedUsers;
    }

    /**
     * Share a list with a new user. The user this list is being shared with must not be this list's
     * owner, and must not already have access to this list.
     *
     * @param user the User to share this list with
     */
    public void addSharedUser(User user) {
        if (user != this.owner && !this.sharedUsers.contains(user)) {
            this.sharedUsers.add(user);
            user.addSharedList(this);
        }
    }

    /**
     * Remove a shared user from this list. Also unmarks this list as shared for the user, in
     * order to maintain bidirectional relationship from list to user.
     *
     * @param user the User to remove as a shared user
     */
    public void removeSharedUser(User user) {
        if (this.sharedUsers.remove(user)) {
            user.removeSharedList(this);
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

    /**
     * Get whether this list is a template
     *
     * @return true if this list is a template, false otherwise
     */
    public boolean isTemplate() {
        return isTemplate;
    }

    /**
     * Set whether this list is a template
     *
     * @param isTemplate a boolean marking if this list is a template
     */
    public void setTemplate(boolean isTemplate) {
        this.isTemplate = isTemplate;
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

    /**
     * Serializer class for a GroceryLists's owner.
     * <p>
     * The serialization of GroceryList does not need to return full information on the list's owner. Rather,
     * it only requires some sort of identifier.
     */
    private static class OwnerSerializer extends JsonSerializer<User> {

        /**
         * Takes the User object, which should be the owner of a GroceryList, and returns just
         * its username as a string
         *
         * @param value       the Users to serialize
         * @param gen         a JsonGenerator to output resulting JSON
         * @param serializers a SerializerProvider that can be used to get serializers
         *                    for serializing Objects value contains, if any.
         * @throws IOException if there is an error writing JSON content
         */
        @Override
        public void serialize(User value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.getUsername());
        }
    }

    /**
     * Serializer class for the shared users of this list
     * <p>
     * The serialization of GroceryList does not need to return full information on the list's shared
     * users. Rather, it only requires some sort of identifier for each user.
     */
    private static class SharedUserSerializer extends JsonSerializer<List<User>> {

        /**
         * Takes the User object, which should be the owner of a GroceryList, and returns just
         * its username as a string
         *
         * @param value       the Users to serialize
         * @param gen         a JsonGenerator to output resulting JSON
         * @param serializers a SerializerProvider that can be used to get serializers
         *                    for serializing Objects value contains, if any.
         * @throws IOException if there is an error writing JSON content
         */
        @Override
        public void serialize(List<User> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            String[] usernames = value.stream().map(User::getUsername).collect(Collectors.toList()).toArray(new String[1]);
            gen.writeArray(usernames, 0, usernames.length);
        }
    }
}
