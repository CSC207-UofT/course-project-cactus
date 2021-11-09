package com.saguaro.controller;

import com.saguaro.entity.User;
import com.saguaro.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public User login(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = userService.login(username, password);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username/password invalid");
        }

        return user;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterPayload payload) {
        User user = userService.registerNewUser(payload.getUsername(),
                payload.getPassword(),
                payload.getName());

        if (user == null) {
            // TODO: create exception handler with better exceptions
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }

        return user;
    }

    private static class RegisterPayload {
        String name;
        String username;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        String password;
    }

    @PostMapping("/logout")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        userService.logout(username);
    }
}
