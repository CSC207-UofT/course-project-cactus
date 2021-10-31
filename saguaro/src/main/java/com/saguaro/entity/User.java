/*
 * This file defines a User class.
 */
package com.saguaro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * User Entity
 */
@javax.persistence.Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    private String password;

    private String name;

    @ManyToMany(
            fetch = FetchType.EAGER
            )
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
    private Collection<Role> roles;

    private String token;

    @OneToMany(mappedBy = "user")
    private Collection<GroceryList> lists;

    public User() {
        this.roles = new ArrayList<>();
        this.lists = new ArrayList<>();
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

        this.roles = new ArrayList<Role>();
        this.roles.add(new Role("ROLE_USER"));
    }

    public long getId() {
        return this.id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword(){
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

    void addGroceryList(GroceryList list) {
        this.lists.add(list);
    }

    void removeGroceryList(GroceryList list) {
        this.lists.remove(list);
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
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
}
