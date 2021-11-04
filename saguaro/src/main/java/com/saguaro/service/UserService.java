package com.saguaro.service;

import com.saguaro.entity.Role;
import com.saguaro.entity.User;
import com.saguaro.repository.RoleRepository;
import com.saguaro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User login(String username, String password) {
        User user = userRepository.findUserByUsername(username);

        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                String newToken = UUID.randomUUID().toString();
                user.setToken(newToken);
                userRepository.save(user);
                return user;
            }
        }
        return null;
    }

    public void logout(String username) {
        User user = userRepository.findUserByUsername(username);
        user.setToken(null);
        userRepository.save(user);
    }

    public User registerNewUser(String username, String password, String name) {
        if (userRepository.existsByUsername(username)) {
            return null;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setName(name);

        Role userRole = roleRepository.findRoleByName("ROLE_USER");
        newUser.addRole(userRole);

        return userRepository.save(newUser);
    }

    public User findByToken(String token) {
        return userRepository.findUserByToken(token);
    }
}