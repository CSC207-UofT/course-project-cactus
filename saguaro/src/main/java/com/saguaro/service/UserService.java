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
            if (passwordEncoder.encode(password).equals(user.getPassword())) {
                String newToken = UUID.randomUUID().toString();
                user.setToken(newToken);

                return user;
            }
        }
        return null;
    }

    public User registerNewUser(String username, String password, String name) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setName(name);

        Role userRole = roleRepository.findRoleByName("ROLE_USER");
        newUser.addRole(userRole);

        return userRepository.save(newUser);
    }
}
