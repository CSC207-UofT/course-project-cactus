/*
 * This file defines a User class.
 */
package com.cactus;

public class User {

    private String username;
    private String password;
    private String name;
    private int id;


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
        this.id = (int) Math.floor(Math.random()*(99999999-10000000+1)+10000000);
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
    public int getId(){
        return id;
    }


    public void setUsername(String username){
        this.username = username;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setId(int id){
        this.id = id;
    }


}
