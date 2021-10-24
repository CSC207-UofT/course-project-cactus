/*
 * This file defines a User class.
 */
package com.saguaro.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

    public User() {}

    /**
     * Creates a new User with the given name, username, password and id number.
     *
     * @param name A String containing the User's name.
     * @param username A String containing the User's username.
     * @param password A String containing the User's password.
     */
    public User(String name, String username, String password){
        this.name = name;
        this.username = username;
        this.password = password;
        this.id = 0L;
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

}
