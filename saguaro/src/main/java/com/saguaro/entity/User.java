/*
 * This file defines a User class.
 */
package com.saguaro.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * User Entity
 */
@javax.persistence.Entity
public class User implements Entity {

    private String username;
    private String password;
    private String name;

    @Id @GeneratedValue
    private long id;

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

    @Override
    public long getId() {
        return this.id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }
}
