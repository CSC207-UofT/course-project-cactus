package com.saguaro.controller;

import com.saguaro.entity.User;
import com.saguaro.exception.InvalidLoginException;
import com.saguaro.exception.InvalidParamException;
import com.saguaro.exception.ResourceNotFoundException;
import com.saguaro.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * REST controller defining endpoints related to user management. The following
 * endpoints are defined:
 * <ul>
 *     <li>login
 *     <li>register
 *     <li>logout
 * </ul>
 *
 * @author Charles Wong
 */
@RestController
public class UserController {

    /**
     * A user management Service to delegate logic to
     */
    private final UserService userService;

    /**
     * Constructs this user controller given a UserService.
     *
     * Notice that by default, Spring will attempt to autowire the only
     * constructor of a class.
     *
     * @param userService a UserService to provide logic for this controller
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Given a string username and password, attempts to authenticate a client. If
     * successful, the corresponding User object is returned, which can be serialized
     * into a JSON object containing the following properties:
     * <ul>
     *     <li>id
     *     <li>name
     *     <li>username
     *     <li>roles
     *     <li>token
     * </ul>
     *
     * If the provided username does not exist, or the password does not match the
     * username, then the login operation is unsuccessful and an InvalidLoginException
     * is thrown.
     *
     * A new token is provided on every successful login, and any prior tokens are
     * invalidated; at most one token will be valid for a user at any given time.
     * Tokens are non-expiring.
     *
     * @param username a String username to authenticate
     * @param password a String password to authenticate
     * @return the User that was successfully authenticated
     * @throws InvalidLoginException if the login operation was unsuccessful
     */
    @PostMapping("/login")
    public User login(@RequestParam("username") String username,
                      @RequestParam("password") String password) throws InvalidLoginException {
        return userService.login(username, password);
    }

    /**
     * Register a new user with Saguaro given a valid RegisterPayload. A valid payload
     * must include the following fields:
     * <ul>
     *     <li>name
     *     <li>username
     *     <li>password
     * </ul>
     * All fields must not be blank; they must be non-null and contain at least one
     * non-whitespace character. Invalid payloads will cause an InvalidParamException
     * to be thrown.
     *
     * On successful registration, the newly created User object is returned. Note that
     * registering a user does not log them in; the object returned will have a null
     * token attribute.
     *
     * @param payload a RegisterPayload containing information about the user to register
     * @return the User that was registered
     * @throws InvalidParamException if an invalid RegisterPayload was provided
     * @see RegisterPayload
     */
    @PostMapping("/register")
    public User register(@Validated(RegisterGroup.class) @RequestBody UserPayload payload) throws InvalidParamException {
        return userService.registerNewUser(payload.username,
                payload.password,
                payload.name);
    }

    /**
     * Logout an authenticated user. This endpoint is a protected resource, meaning
     * that a valid token must have been provided in an Authorization header to reach
     * this method. On successful logout, that token will be invalidated.
     */
    @PostMapping("/logout")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        userService.logout(username);
    }

    /**
     * Given a valid UserPayload, replaced the logged-in user's attributes with any
     * non-null attributes of the payload. Note that a user's username cannot be
     * changed; the username property of the payload is ignored by this method.
     *
     * @param payload the UserPayload containing user attributes to change
     * @return the newly saved User object
     */
    @PutMapping("/api/edit-user")
    public User editUser(@Validated(EditGroup.class) @RequestBody UserPayload payload) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        return userService.edit(payload.name, payload.password, username);
    }

    /**
     * Given a username, add that user as a friend of the currently authenticated user.
     * <p>
     * If the provided username to add as a friend does not exist, a
     * ResourceNotFoundException will be thrown.
     *
     * @param friendUsername the String username of the user to add as a friend
     * @return the newly modified User object
     * @throws ResourceNotFoundException if the given username to add as a friend does not
     *                                   exist
     */
    @PostMapping("api/add-friend")
    public User addFriend(@RequestParam("username") String friendUsername)
            throws ResourceNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        return userService.addFriend(friendUsername, username);
    }

    /**
     * Given a username, remove that user from the friends of the currently authenticated user.
     * <p>
     * If the provided username does not match a user that is an existing friend of the
     * authenticated user, a ResourceNotFoundException is thrown.
     *
     * @param friendUsername the String username of the user to remove from friends
     * @return the newly modified User object
     * @throws ResourceNotFoundException if the user with the given username is not an existing
     *                                   friend
     */
    @DeleteMapping("api/remove-friend")
    public User removeFriend(@RequestParam("username") String friendUsername)
            throws ResourceNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        return userService.removeFriend(friendUsername, username);
    }

    /**
     * Used to specify the validation strategies for registration inputs
     */
    private interface RegisterGroup {}

    /**
     * Used to specify the validation strategies for edit user info inputs
     */
    private interface EditGroup {}

    /**
     * Data container class for the client specifiable user data, to deserialize
     * JSON into. Used by the register user and edit user information endpoints.
     */
    private static class UserPayload {

        /**
         * Name of a user
         */
        @NullOrNotBlank(groups = EditGroup.class)
        @NotBlank(groups = RegisterGroup.class)
        String name;

        /**
         * Username of a user
         */
        @NotBlank(groups = RegisterGroup.class)
        String username;

        /**
         * Password of a user
         */
        @NullOrNotBlank(groups = EditGroup.class)
        @NotBlank(groups = RegisterGroup.class)
        String password;

        /**
         * Setter for the name attribute, used for deserialization
         *
         * @param name a String to set the name attribute to
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Setter for the username attribute, used for deserialization
         *
         * @param username a String to set the username attribute to
         */
        public void setUsername(String username) {
            this.username = username;
        }

        /**
         * Setter for the password attribute, used for deserialization
         *
         * @param password a String to set the password attribute to
         */
        public void setPassword(String password) {
            this.password = password;
        }
    }
}
