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

/**
 * This class is a service class providing all logic for user related operations. It is responsible
 * for interfacing with repository interfaces to fetch/persist User entities.
 *
 * @author Charles Wong
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    /**
     * Repository interface for user tables
     */
    private UserRepository userRepository;

    /**
     * Repository interface for role tables
     */
    private RoleRepository roleRepository;

    /**
     * Bean that handles password hashing
     */
    private PasswordEncoder passwordEncoder;

    /**
     * Constructs a UserService, injecting all requires dependencies.
     * <p>
     * Note that this constructor is automatically picked up by Spring for autowiring.
     *
     * @param userRepository  a UserRepository instance to support this service
     * @param roleRepository  a RoleRepository instance to support this service
     * @param passwordEncoder a PasswordEncoder instance to support this service
     */
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Given a username and password, attempt to log in the user. If successful, an authentication
     * token is generated for the user, and attached to the returned User object. If the user already
     * has an authentication token, a new one will be generated regardless, which will overwrite
     * the old token.
     * <p>
     * If the provided combination does not match an existing user, then an InvalidLoginException is thrown.
     *
     * @param username the String username attempting to log in
     * @param password the String password of the user attempting to log in
     * @return the corresponding User object to the inputted username and password, with a login token
     * @throws InvalidLoginException if the provided username and password does not match an existing user
     */
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

    /**
     * Given a username, logout the corresponding user by invalidating their
     * authentication token.
     * <p>
     * This method assumes that the provided username is one that belongs to an
     * existing user, since a user must be authenticated with Saguaro to logout.
     *
     * @param username the String username of the user to logout
     */
    @Transactional
    public void logout(String username) {
        User user = userRepository.findUserByUsername(username);
        user.setToken(null);
        userRepository.save(user);
    }

    /**
     * Register a new user with Saguaro, using the provided username, password, and name. The password is
     * hashed using the PasswordEncoder bean injected into this service class, and saved. All users are given
     * the role "ROLE_USER", which is assumed to exist in the role database table before any user is registered.
     * <p>
     * If a username attempting to register already belongs to an existing user, an InvalidParamException is thrown.
     * <p>
     * Note that registering a user does not log them in.
     *
     * @param username the String username to register
     * @param password the String password of the user attempting to register
     * @param name     the String name of the user attempting to register
     * @return a fully populated User object containing the details of the newly registered user
     * @throws InvalidParamException if the username provided already belongs to an existing user
     */
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

    /**
     * Attempt to find the user that holds the provided authentication token. If none exist,
     * then return null. Otherwise, populate a {@link UserDetails} object with the user's details
     * and return that. This method is intended to be used as part of Saguaro's security flow.
     *
     * @param token the String authentication token to attempt to match
     * @return a fully populated UserDetails object containing the found user, or null if none is found
     */
    public UserDetails findByToken(String token) {
        User user = userRepository.findUserByToken(token);

        if (user == null) {
            return null;
        }

        return buildSpringUser(user);
    }

    /**
     * Build a UserDetails object using the provided Saguaro User object
     *
     * @param user the User to build the UserDetails from
     * @return a UserDetails object populated with the details of the user
     */
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
