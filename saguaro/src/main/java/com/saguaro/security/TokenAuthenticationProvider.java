package com.saguaro.security;

import com.saguaro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * This class defines an AuthenticationProvider that provides token
 * authentication services.
 */
@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

    /**
     * Saguaro's User service
     */
    @Autowired
    UserService userService;

    /**
     * Authenticate an Authentication object that was caught through Saguaro's filter chain. In
     * particular, this method attempts to match the token provided in the Authentication object
     * with an existing user's token.
     *
     * @param authentication the Authentication object to authenticate
     * @return a fully populated and valid Authentication object, if authentication was successful
     * @throws AuthenticationException if authentication was unsuccessful
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getCredentials().toString();
        UserDetails userDetails = userService.findByToken(token);

        if (userDetails == null) {
            throw new BadCredentialsException("Cannot find user by token");
        }

        // notice that in this instance, the token is stored into the password field of UserDetails
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }

    /**
     * Tests for whether the Authentication object passed into this provider is supported.
     *
     * This provider supports instances of UsernamePasswordAuthenticationToken, which is used
     * as a carrier for authentication tokens.
     *
     * @param authentication the Class of some object passed into this provider for authentication
     * @return true if the class is supported, false otherwise
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
