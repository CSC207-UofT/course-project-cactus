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
 * This class implements a User entity, and is used by Hibernate to generate a
 * corresponding database representation.
 * <p>
 * Every user is identified with a numerical ID, and a unique username. A user must also
 * have a password, which is stored as a bcrypt hash, and a plaintext name. By default,
 * all users are automatically assigned the ROLE_USER role on registration. If a user is
 * currently logged in, they will have a string authentication token associated with
 * their account. Finally, every user can own grocery lists.
 *
 * @author Charles Wong
 */
@Entity
public class User {

    /**
     * An ID for this user
     */
    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private long id;

    /**
     * The username of this user
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * The hashed password of this user
     */
    @JsonIgnore
    private String password;

    /**
     * The name of this user
     */
    private String name;

    /**
     * The roles this user has
     */
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

    /**
     * The authentication token of this user
     */
    private String token;

    /**
     * The grocery lists of this user
     *
     * @see GroceryList
     */
    @JsonIgnore
    @OneToMany(mappedBy = "owner")
    private List<GroceryList> lists;

    /**
     * The grocery lists that have been shared with this user
     */
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

    /**
     * Creates a User with no roles and grocery lists. Does not initialize any
     * attributes; all attributes must be set explicitly to persist this entity.
     */
    public User() {
        this.roles = new ArrayList<>();
        this.lists = new ArrayList<>();
        this.sharedLists = new ArrayList<>();

        this.friends = new ArrayList<>();
        this.befriended = new ArrayList<>();
    }

    /**
     * Get the ID of this user
     *
     * @return a long representing the ID of this user
     */
    public long getId() {
        return this.id;
    }

    /**
     * Get the name of this user
     *
     * @return the String name of this user
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this user
     *
     * @param name the String to set the name of this user to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the username of this user
     *
     * @return the String username of this user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username of this user
     *
     * @param username the String to set the username of this user to
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the hashed password of this user
     *
     * @return a String representing the hashed password of this user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password of this user. For security, the password of a user should
     * not be saved as plaintext; passwords of User objects should be set as hashes of
     * the desired password.
     *
     * @param password the String to set the password of this user to
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the authentication token of this password. This will return null if the user
     * is not currently logged in.
     *
     * @return the String authentication token of this user
     */
    public String getToken() {
        return this.token;
    }

    /**
     * Set the authentication token of this user
     *
     * @param token the String to set the token of this user to
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Give this user a role
     *
     * @param role the Role to give this user
     * @see Role
     */
    public void addRole(Role role) {
        this.roles.add(role);
        role.addUser(this);
    }

    /**
     * Get all grocery lists belonging to this user
     *
     * @return a List of GroceryLists belonging to this user
     */
    @JsonIgnore
    public List<GroceryList> getGroceryLists() {
        return this.lists;
    }

    /**
     * Add a grocery list to the lists this user owns
     *
     * @param list the GroceryList to add to the ones this user owns
     */
    void addGroceryList(GroceryList list) {
        this.lists.add(list);
    }

    /**
     * Remove a grocery list from the lists this user owns
     *
     * @param list the GroceryList to remove from this user
     */
    void removeGroceryList(GroceryList list) {
        this.lists.remove(list);
    }

    /**
     * Gets the authorities this user has. In Saguaro, this is synonymous with all
     * Roles this user has.
     *
     * @return a Collection of GrantedAuthorities belonging to this user
     */
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

    /**
     * Compares an object against this user. The object being compared is equal
     * to this User if and only if it is a User with the same ID, name, username,
     * password, roles, and token. Only the token is allowed to be null.
     *
     * @param o an Object to compare against
     * @return true if the object being compared is equal to this object, false otherwise
     */
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
