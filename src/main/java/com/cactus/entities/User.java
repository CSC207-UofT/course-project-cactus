/*
 * This file defines a User class.
 */
package com.cactus.entities;

public class User implements Entity {

    private String username;
    private String password;
    private String name;

    private long id;

    /**
     * Creates a new User with the given name, username, passward and id number.
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

//    public void setUsername(String username){
//        this.username = username;
//    }

    public void setName(String name){
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(long id) {
        this.id = id;
    }

}
