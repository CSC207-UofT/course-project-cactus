package com.saguaro.security;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class defines a token authentication filter for Saguaro.
 *
 * @author Charles Wong
 */
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * Constructs a new TokenAuthenticationFilter, that applies to some specified set of URLs.
     *
     * @param requiresAuthenticationRequestMatcher a RequestMatcher that specifies which URLs to filter
     */
    public TokenAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    /**
     * Attempt to authenticate a user using the Authorization header provided in their request.
     *
     * @param request  the HttpServletRequest that requires authentication
     * @param response the HttpServletResponse that is returned from the request
     * @return an Authentication object containing authentication details provided in the request
     * @throws AuthenticationException if no Authorization head was provided with the request, or if the authentication
     *                                 attempt throws an exception
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String token = request.getHeader("AUTHORIZATION");

        if (token == null) {
            throw new AuthenticationCredentialsNotFoundException("No AUTHORIZATION header found");
        }
        token = token.replace("Bearer", "").trim();

        Authentication requestAuth = new UsernamePasswordAuthenticationToken(token, token);
        return getAuthenticationManager().authenticate(requestAuth);
    }

    /**
     * Define actions to perform upon successful authentication.
     * <p>
     * In this case, set the SecurityContext with the valid Authentication object, and
     * continue this application's filter chain.
     *
     * @param request    the HttpServletRequest that was succesfully authenticated
     * @param response   the HttpServletResponse that will be returned
     * @param chain      the FilterChain of this application
     * @param authResult the Authentication object containing the authentication result
     * @throws IOException      if continuing the filter chain throws some exception
     * @throws ServletException if continuing the filter chain throws some exception
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

}
