/*
 * This file defines a User class.
 */
package com.saguaro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User Entity
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    private String password;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "user_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id"
            )
    )
    private Collection<Role> roles;

    private String token;

    @JsonIgnore
    @OneToMany(mappedBy = "owner")
    private List<GroceryList> lists;

    @JsonIgnore
    @ManyToMany(mappedBy = "sharedUsers")
    private List<GroceryList> sharedLists;

    /**
     * The users this user has befriended
     */
    @JsonSerialize(using = FriendSerializer.class)
    @ManyToMany
    @JoinTable(
            name = "USER_FRIENDS",
            joinColumns = @JoinColumn(
                    name = "USER_ID",
                    referencedColumnName = "USER_ID"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "FRIEND_ID",
                    referencedColumnName = "USER_ID"
            )
    )
    private List<User> friends;
    // using Sets "breaks" Hibernate
    // see https://stackoverflow.com/questions/5031614/the-jpa-hashcode-equals-dilemma

    /**
     * The users this user has been befriended by
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "friends")
    private List<User> befriended;

    public User() {
        this.roles = new ArrayList<>();
        this.lists = new ArrayList<>();
        this.sharedLists = new ArrayList<>();

        this.friends = new ArrayList<>();
        this.befriended = new ArrayList<>();
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void addRole(Role role) {
        this.roles.add(role);
        role.addUser(this);
    }

    public List<GroceryList> getGroceryLists() {
        return this.lists;
    }

    void addGroceryList(GroceryList list) {
        this.lists.add(list);
    }

    void removeGroceryList(GroceryList list) {
        this.lists.remove(list);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    /**
     * Get the list of users that are friends of this user
     *
     * @return the friends of this user
     */
    public List<User> getFriends() {
        return this.friends;
    }

    /**
     * Add a user to this user's list of friends
     *
     * @param user the User to add as a friend
     */
    public void addFriend(User user) {
        if (user != this && !this.friends.contains(user)) {
            this.friends.add(user);
            user.addBefriended(this);
        }
    }

    /**
     * Remove a user from this user's list of friends.
     *
     * @param user the User to remove as a friend
     */
    public void removeFriend(User user) {
        if (this.friends.remove(user)) {
            user.removeBefriended(user);
        }
    }

    /**
     * Record a user as having befriended this user. Used to maintain the bidirectional
     * relationship.
     *
     * @param user the User to mark as having befriended this user
     */
    private void addBefriended(User user) {
        this.befriended.add(user);
    }

    /**
     * Record a user as not being befriended by this user anymore. Used to maintain the bidirectional
     * relationship.
     *
     * @param user the User to mark as not being befriended by this user anymore
     */
    private void removeBefriended(User user) {
        this.befriended.remove(user);
    }

    /**
     * Get all the lists that were shared with this user
     *
     * @return a List of GroceryLists that were shared with this user
     */
    public List<GroceryList> getSharedLists() {
        return this.sharedLists;
    }

    /**
     * Add a GroceryList to the list of lists that were shared with this user
     *
     * @param list the GroceryList to mark as being shared with this user
     */
    void addSharedList(GroceryList list) {
        this.sharedLists.add(list);
    }

    /**
     * Remove a list from the grocery lists that were shared with this user
     *
     * @param list the GroceryList to remove from this user's shared lists
     */
    void removeSharedList(GroceryList list) {
        this.sharedLists.remove(list);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id
                && username.equals(user.username)
                && password.equals(user.password)
                && name.equals(user.name)
                && roles.equals(user.roles)
                && Objects.equals(token, user.token);
    }

    /**
     * Generate a hash for this User
     *
     * @return an integer hash for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, name, roles, token);
    }

    /**
     * Serializer class for a user's friends
     */
    private static class FriendSerializer extends JsonSerializer<List<User>> {

        /**
         * Takes the set of users that are a user's friends, and serializes only their
         * usernames, outputting an array of strings.
         *
         * @param value       the Set of Users to serialize
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
