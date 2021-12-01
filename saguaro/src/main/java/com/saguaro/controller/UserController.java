package com.saguaro.controller;

import com.saguaro.entity.User;
import com.saguaro.exception.InvalidLoginException;
import com.saguaro.exception.InvalidParamException;
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
