/*
 * This file defines a User class.
 */
package com.cactus.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.junit.Ignore;

import java.sql.Timestamp;

/**
 * User Entity
 */
public class User {

    private String username;
    private String password;
    private String name;
    private String token;

    /**
     * Creates a new User with the given name, username, password and id number.
     *
     * @param name A String containing the User's name.
     * @param username A String containing the User's username.
     * @param password A String containing the User's password.
     * @param token A String containing a token to signify User was successfully retrieved from server
     */

    public User(String name, String username, String password, String token){
        this.name = name;
        this.username = username;
        this.password = password;
        this.token = token;
    }

    public User(){
        super();
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
    public String getToken(){
        return token;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setToken(String token){
        this.token = token;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString(){
        return "The user's name is: " + name + " and has a username of " + username + "and token of " + token;
    }
}
