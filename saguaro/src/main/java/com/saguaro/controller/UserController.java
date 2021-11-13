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
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    public User register(@Validated @RequestBody RegisterPayload payload) throws InvalidParamException {
        return userService.registerNewUser(payload.username,
                payload.password,
                payload.name);
    }

    private static class RegisterPayload {
        @NotBlank
        String name;

        @NotBlank
        String username;

//        public String getName() {
//            return name;
//        }
//
        public void setName(String name) {
            this.name = name;
        }
//
//        public String getUsername() {
//            return username;
//        }
//
        public void setUsername(String username) {
            this.username = username;
        }
//
//        public String getPassword() {
//            return password;
//        }
//
        public void setPassword(String password) {
            this.password = password;
        }

        @NotBlank
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
