package com.saguaro.service;

import com.saguaro.entity.Role;
import com.saguaro.entity.User;
import com.saguaro.exception.InvalidLoginException;
import com.saguaro.exception.InvalidParamException;
import com.saguaro.exception.ResourceNotFoundException;
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

    public User login(String username, String password) throws InvalidLoginException {
        User user = userRepository.findUserByUsername(username);

        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                String newToken = UUID.randomUUID().toString();
                user.setToken(newToken);
                userRepository.save(user);
                return user;
            }
        }

        throw new InvalidLoginException();
    }

    public void logout(String username) {
        User user = userRepository.findUserByUsername(username);
        user.setToken(null);
        userRepository.save(user);
    }

    public User registerNewUser(String username, String password, String name)
            throws InvalidParamException {
        if (userRepository.existsByUsername(username)) {
            throw new InvalidParamException("Could not register user: " + username + "; username already exists");
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

    @Transactional
    public User addFriend(String friendUsername, String username)
            throws ResourceNotFoundException {
        User user = userRepository.findUserByUsername(username);
        User friend = userRepository.findUserByUsername(friendUsername);

        if (friend == null) {
            throw new ResourceNotFoundException(User.class, friendUsername);
        }

        user.addFriend(friend);
        return userRepository.save(user);
    }
}
