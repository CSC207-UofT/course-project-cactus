package com.saguaro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
@javax.persistence.Entity
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
    @OneToMany(mappedBy = "user")
    private List<GroceryList> lists;

    /**
     * Creates a User with no roles and grocery lists. Does not initialize any
     * attributes; all attributes must be set explicitly to persist this entity.
     */
    public User() {
        this.roles = new ArrayList<>();
        this.lists = new ArrayList<>();
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
}
