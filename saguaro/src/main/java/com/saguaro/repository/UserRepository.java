package com.saguaro.repository;

import com.saguaro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface defining user database interaction methods. Methods defined
 * here are automatically generated implementations by Spring through reflection magic.
 * Extending JpaRepository also provides some default repository methods.
 *
 * @author Charles Wong
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user given their username.
     *
     * @param username the String username of the User to find
     * @return the User with the given username, or null if it does not exist
     */
    User findUserByUsername(String username);

    /**
     * Find a role given their authentication token.
     *
     * @param token the String token of the Role to find
     * @return the User with the given token, or null if none exists
     */
    User findUserByToken(String token);

    /**
     * Check if a user with the given username exists in the database.
     *
     * @param username the String username to check
     * @return true if a User with the given username exists, false otherwise
     */
    boolean existsByUsername(String username);
}
