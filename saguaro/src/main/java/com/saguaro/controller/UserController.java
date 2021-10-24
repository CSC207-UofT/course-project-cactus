package com.saguaro.controller;

import com.saguaro.entity.User;
import com.saguaro.repository.UserRepository;
import com.saguaro.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            throw new UsernameNotFoundException("Username/password invalid");
        }

        return user;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterPayload payload){
        return userService.registerNewUser(payload.getUsername(),
                payload.getPassword(),
                payload.getName());
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

    /**
     * @return all users in the database
     */
    @GetMapping("/users")
    List<User> all() {
        return null;
    }
}
