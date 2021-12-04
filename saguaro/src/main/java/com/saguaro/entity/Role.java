package com.saguaro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This class implements a Role entity, and is used by Hibernate to generate a
 * corresponding database representation. This class additionally implements the
 * {@link GrantedAuthority} interface, allowing Roles to be used to authorize access
 * to protected resources by Spring.
 * <p>
 * Every user is by default given the ROLE_USER role on registration.
 *
 * @author Charles Wong
 */
@Entity
public class Role implements GrantedAuthority {

    /**
     * An ID for this role
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The name of this role
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * All users who have this role
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    /**
     * Creates a new role. The name of the role must be set explicitly to persist
     * this entity.
     */
    public Role() {
        this.users = new ArrayList<>();
    }

    /**
     * Creates a role with the given name
     *
     * @param name the name for the role to create
     */
    public Role(String name) {
        this.users = new ArrayList<>();
        this.name = name;
    }

    /**
     * Returns a string representation of the authorities this role grants. This is
     * synonymous with the name of this role for Saguaro.
     *
     * @return a String representation of the authorities this role grants
     * @see GrantedAuthority#getAuthority()
     */
    @Override
    public String getAuthority() {
        return name;
    }

    /**
     * Get the name of this role
     *
     * @return the String name of this role
     */
    public String getName() {
        return name;
    }

    /**
     * Get the ID of this role
     *
     * @return a long representing the ID of this role
     */
    public Long getId() {
        return id;
    }

    /**
     * Add a user to the collection of users who have this role
     *
     * @param user the User to record as having this role
     */
    void addUser(User user) {
        this.users.add(user);
    }

    /**
     * Compares an object against this role. The object being compared is equal
     * to this Role if and only if it is a Role with the same non-null ID and name.
     *
     * @param o an Object to compare against
     * @return true if the object being compared is equal to this object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id.equals(role.id) && name.equals(role.name);
    }
}
