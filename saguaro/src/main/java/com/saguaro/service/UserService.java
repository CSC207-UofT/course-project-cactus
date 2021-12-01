package com.saguaro.service;

import com.saguaro.entity.Role;
import com.saguaro.entity.User;
import com.saguaro.exception.InvalidLoginException;
import com.saguaro.exception.InvalidParamException;
import com.saguaro.repository.RoleRepository;
import com.saguaro.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserService {

    UserRepository userRepository;

    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User login(String username, String password) throws InvalidLoginException {
        User user = userRepository.findUserByUsername(username);

        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                String newToken = UUID.randomUUID().toString();
                user.setToken(newToken);
                userRepository.save(user);
                return user;
            }
        }

        throw new InvalidLoginException();
    }

    @Transactional
    public void logout(String username) {
        User user = userRepository.findUserByUsername(username);
        user.setToken(null);
        userRepository.save(user);
    }

    @Transactional
    public User registerNewUser(String username, String password, String name)
            throws InvalidParamException {
        if (userRepository.existsByUsername(username)) {
            throw new InvalidParamException("Could not register user: " + username + "; username already exists");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setName(name);

        Role userRole = roleRepository.findRoleByName("ROLE_USER");
        newUser.addRole(userRole);

        return userRepository.save(newUser);
    }

    public UserDetails findByToken(String token) {
        User user = userRepository.findUserByToken(token);

        if (user == null) {
            return null;
        }

        return buildSpringUser(user);
    }

    private org.springframework.security.core.userdetails.User buildSpringUser(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getToken(),
                true,
                true,
                true,
                true,
                user.getAuthorities()
        );
    }

    /**
     * Edits the name and password of a User, specified by a username. The name
     * or password can be null, in which case that attribute of the user remains
     * unchanged.
     *
     * The plaintext password is hashed using bcrypt, and then saved.
     *
     * @param name the String name to replace, if not null
     * @param password the String plaintext of the password to replace, if not null
     * @param username the String username of the user to change the attributes of
     * @return the newly saved User object
     */
    @Transactional
    public User edit(String name, String password, String username) {
        User user = userRepository.findUserByUsername(username);

        if (name != null) {
            user.setName(name);
        }

        if (password != null) {
            user.setPassword(passwordEncoder.encode(password));
        }

        return userRepository.save(user);
    }
}
