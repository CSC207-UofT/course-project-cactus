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
    private User owner;
    // we ignore the field, but set its getter as the JsonProperty
    // then we can ignore the setter, and have this field only be
    // serialized, but not deserialized

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
     * Creates a new GroceryList, with null ID, name, and user. The newly created
     * grocery list contains an empty list of items.
     */
    public GroceryList() {
        this.items = new ArrayList<>();
        this.sharedUsers = new ArrayList<>();
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
    @JsonProperty
    @JsonSerialize(using = OwnerSerializer.class)
    public User getOwner() {
        return this.owner;
    }

    /**
     * Set the owner of this list if one is not set already. Once initially set, the
     * owner of a list cannot be changed.
     * <p>
     * The owner of a list is not deserialized, since a user must be authenticated
     * to interact with grocery lists anyways
     *
     * @param owner the User to set as the owner of this list
     */
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

    /**
     * Performs all necessary operations to maintain database relations before deleting
     * this list. This list is removed from the collection of lists the owner User owns,
     * and removes all reference to this list from this list's contained items.
     * <p>
     * This method is called automatically by Hibernate on deletion of this list.
     */
    @PreRemove
    void removeList() {
        this.owner.removeGroceryList(this);

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
