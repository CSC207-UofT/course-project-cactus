package com.saguaro.service;

import com.saguaro.entity.Role;
import com.saguaro.entity.User;
import com.saguaro.repository.RoleRepository;
import com.saguaro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
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

    @Transactional(readOnly = true)
    public UserDetails findByToken(String token) {
        User user = userRepository.findUserByToken(token);

        if (user == null) {
            return null;
        }

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        return buildSpringUser(user, authorities); // roles are loaded lazily, so fetch them here for use
    }

    private org.springframework.security.core.userdetails.User buildSpringUser(User user, Collection<? extends GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getToken(), true, true, true, true, authorities
        );
    }
}
