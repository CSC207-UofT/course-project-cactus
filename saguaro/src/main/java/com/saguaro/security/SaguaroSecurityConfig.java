package com.saguaro.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * This class configures the security setup for Saguaro. As an overview, only
 * the register and login endpoints are unprotected. All other endpoints require
 * some valid authentication token to access. The h2-console does not require
 * authentication through Saguaro, but requires authentication with the database.
 *
 * This class also provides the password encryption bean to the application.
 *
 * @author Charles Wong
 */
@Configuration
public class SaguaroSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * An authentication provider that gives token authentication functionality
     */
    @Autowired
    TokenAuthenticationProvider provider;

    /**
     * A RequestMatcher that will match all protected endpoints
     */
    private static final RequestMatcher REQUEST_MATCHER = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/**"),
            new AntPathRequestMatcher("/logout*")
    );

    /**
     * Configure the AuthenticationManager for Saguaro.
     *
     * In particular, this method attaches a TokenAuthenticationProvider to
     * the AuthenticationManager.
     *
     * @param builder an AuthenticationManagerBuilder, provided by Spring
     */
    @Override
    public void configure(AuthenticationManagerBuilder builder) {
        builder.authenticationProvider(provider);
    }

    /**
     * Configure WebSecurity for Saguaro.
     *
     * In particular, this method defines that the login and register endpoints should
     * be unprotected. The h2-console is also left unprotected by Saguaro, but it has its
     * own authentication requirements.
     *
     * @param web a WebSecurity object, provided by Spring
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/login*", "/register*", "/h2-console/**");
    }

    /**
     * Configures general HTTP security for Saguaro.
     *
     * The following features are configured:
     * <ul>
     *     <li>Do not create any session for users</li>
     *     <li>Disable Spring's default login/logout</li>
     *     <li>Disable CSRF protection (Saguaro does not provide support for browser access)</li>
     *     <li>Add a token authentication filter, to catch requests using Saguaro's token auth</li>
     *     <li>Authorize any requests to protected endpoints if a user is authenticated</li>
     * </ul>
     *
     * @param http an HttpSecurity object, provided by Spring
     * @throws Exception if any security step throws an Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .logout().disable()
                .csrf().disable()
                .formLogin().disable()
                .authenticationProvider(provider)
                .addFilterBefore(createTokenFilter(), AnonymousAuthenticationFilter.class)
                .authorizeRequests()
                .requestMatchers(REQUEST_MATCHER)
                .authenticated();

        http.headers().frameOptions().disable(); // needed for h2 console
    }

    /**
     * Expose a PasswordEncoder bean, to encrypt passwords.
     *
     * The algorithm used in Saguaro to encrypt passwords is bcrypt.
     *
     * @return a PasswordEncoder object for use elsewhere in Saguaro
     */
    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(10);
    }

    /**
     * Initialize a token authentication filter as a bean. The filter is
     * attached to this application's AuthenticationManager.
     *
     * @return the newly created authentication filter
     * @throws Exception if getting this application's AuthenticationManager throws an Exception
     */
    @Bean
    AbstractAuthenticationProcessingFilter createTokenFilter() throws Exception {
        TokenAuthenticationFilter filter = new TokenAuthenticationFilter(REQUEST_MATCHER);
        filter.setAuthenticationManager(authenticationManager());

        return filter;
    }
}
