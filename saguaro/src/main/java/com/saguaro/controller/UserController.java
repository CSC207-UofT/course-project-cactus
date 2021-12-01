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

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public User login(@RequestParam("username") String username,
                      @RequestParam("password") String password) throws InvalidLoginException {
        return userService.login(username, password);
    }

    @PostMapping("/register")
    public User register(@Validated(RegisterGroup.class) @RequestBody UserPayload payload) throws InvalidParamException {
        return userService.registerNewUser(payload.username,
                payload.password,
                payload.name);
    }

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

    private interface RegisterGroup {}
    private interface EditGroup {}

    private static class UserPayload {
        @NullOrNotBlank(groups = EditGroup.class)
        @NotBlank(groups = RegisterGroup.class)
        String name;

        @NotBlank(groups = RegisterGroup.class)
        String username;

        @NullOrNotBlank(groups = EditGroup.class)
        @NotBlank(groups = RegisterGroup.class)
        String password;

        public void setName(String name) {
            this.name = name;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
