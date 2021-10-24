/*
 * This file defines a User class.
 */
package com.saguaro.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User Entity
 */
@javax.persistence.Entity
public class User {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id"
            )
    )
    private Collection<Role> role;

    public User() {
        this.role = new ArrayList<>();
    }

    /**
     * Creates a new User with the given name, username, password and id number.
     *
     * @param name A String containing the User's name.
     * @param username A String containing the User's username.
     * @param password A String containing the User's password.
     *
     * @deprecated Use explicit setters to set attributes
     */
    public User(String name, String username, String password){
        this.name = name;
        this.username = username;
        this.password = password;

        this.role = new ArrayList<Role>();
        this.role.add(new Role("ROLE_USER"));
    }

    public String getName(){
        return name;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public long getId() {
        return this.id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role.add(new Role(role));
    }
}
