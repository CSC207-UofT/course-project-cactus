package com.saguaro.repository;

import com.saguaro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Extending the JpaRepository interface will give us access
     * to the in-memory database. We specify that the repository will
     * hold Users, with its ID (primary key) being of type Long.
     */
    User findUserByUsername(String username);

    User findUserByToken(String token);

    boolean existsByUsername(String username);
}
