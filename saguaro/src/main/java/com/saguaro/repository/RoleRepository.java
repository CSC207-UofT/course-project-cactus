package com.saguaro.repository;

import com.saguaro.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface defining role database interaction methods. Methods defined
 * here are automatically generated implementations by Spring through reflection magic.
 * Extending JpaRepository also provides some default repository methods.
 *
 * @author Charles Wong
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find a role given its name.
     *
     * @param name the String name of the Role to find
     * @return the Role with the given name, or null if it does not exist
     */
    Role findRoleByName(String name);
}
