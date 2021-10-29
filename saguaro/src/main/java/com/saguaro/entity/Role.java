package com.saguaro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public Collection<User> getUsers() {
        return users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id.equals(role.id) && name.equals(role.name);
    }
}
