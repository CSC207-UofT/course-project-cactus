/*
 * This file defines a User class.
 */
package com.cactus.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;

/**
 * User Entity
 */
public class User {

    private String username;
    private String password;
    private String name;
    private String token;
    private long id;

    /**
     * Creates a new User with the given name, username, password and id number.
     *
     * @param name A String containing the User's name.
     * @param username A String containing the User's username.
     * @param password A String containing the User's password.
     * @param token A String containing a token to signify User was successfully retrieved from server
     * @param id A long value serving as an identifier for the User.
     */

    public User(String name, String username, String password, String token, long id){
        this.name = name;
        this.username = username;
        this.password = password;
        this.token = token;
        this.id = id;
    }

    public User(){

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
    public long getId(){
        return id;
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
    public void setId(long id){
        this.id = id;
    }

}
