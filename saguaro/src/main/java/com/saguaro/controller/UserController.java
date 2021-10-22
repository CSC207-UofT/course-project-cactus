package com.saguaro.controller;

import com.saguaro.entity.User;
import com.saguaro.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * @return all users in the database
     */
    @GetMapping("/users")
    List<User> all() {
        return repository.findAll();
    }
}
