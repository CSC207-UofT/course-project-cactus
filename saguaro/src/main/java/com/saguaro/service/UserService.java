package com.saguaro.service;

import com.saguaro.entity.Role;
import com.saguaro.entity.User;
import com.saguaro.exception.InvalidLoginException;
import com.saguaro.exception.InvalidParamException;
import com.saguaro.exception.ResourceNotFoundException;
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
     * <p>
     * The plaintext password is hashed using bcrypt, and then saved.
     *
     * @param name     the String name to replace, if not null
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

    /**
     * Given a friend username and a username, add the User represented by the former as
     * a friend of the latter. This method assumes that the username (but not the
     * friend's username) belongs to a valid user, since a user must be authenticated
     * in order to add a friend.
     * <p>
     * If the provided friend username does not map to an existing user, a
     * ResourceNotFoundException is thrown.
     *
     * @param friendUsername the String username of the user to add as a friend
     * @param username       the String username of the user adding the friend
     * @return the newly modified User object
     * @throws ResourceNotFoundException if the given username to add as a friend does not
     *                                   exist
     */
    @Transactional
    public User addFriend(String friendUsername, String username)
            throws ResourceNotFoundException {
        User user = userRepository.findUserByUsername(username);
        User friend = userRepository.findUserByUsername(friendUsername);

        if (friend == null) {
            throw new ResourceNotFoundException(User.class, friendUsername);
        }

        user.addFriend(friend);
        return userRepository.save(user);
    }

    /**
     * Given a friend username and a username, remove the User represented by the former from
     * the friends of the latter. This method assumes that the username (but not the
     * friend's username) belongs to a valid user, since a user must be authenticated
     * in order to remove a friend.
     * <p>
     * If the provided friend username does not map to an existing friend of the user, a
     * ResourceNotFoundException is thrown.
     *
     * @param friendUsername the String username of the user to remove from friends
     * @param username       the String username of the user removing the friend
     * @return the newly modified User object
     * @throws ResourceNotFoundException if the user with the given username is not an existing
     *                                   friend
     */
    @Transactional
    public User removeFriend(String friendUsername, String username)
            throws ResourceNotFoundException {
        User user = userRepository.findUserByUsername(username);
        User friend = userRepository.findUserByUsername(friendUsername);

        if (friend == null) {
            throw new ResourceNotFoundException(User.class, friendUsername);
        }

        user.removeFriend(friend);
        return userRepository.save(user);
    }
}
