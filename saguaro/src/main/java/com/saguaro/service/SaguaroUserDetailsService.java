package com.saguaro.service;

import com.saguaro.entity.User;
import com.saguaro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * User extends UserDetails, it may be advantageous to use this class
 * in order to avoid bloating user returns
 */
@Service
public class SaguaroUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid username/password");
        }

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        return buildSpringUser(user, authorities);
    }

    private org.springframework.security.core.userdetails.User buildSpringUser(User user, Collection<? extends GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), user.getPassword(), true, true, true, true, authorities
        );
    }
}
