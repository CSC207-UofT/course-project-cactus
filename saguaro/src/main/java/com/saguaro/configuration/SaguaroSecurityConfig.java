package com.saguaro.configuration;

import com.saguaro.security.TokenAuthenticationFilter;
import com.saguaro.security.TokenAuthenticationProvider;
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

@Configuration
public class SaguaroSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    TokenAuthenticationProvider provider;

    private static final RequestMatcher REQUEST_MATCHER = new OrRequestMatcher(
            new AntPathRequestMatcher("/test*"),
            new AntPathRequestMatcher("/api/*"),
            new AntPathRequestMatcher("/logout*")
    );

    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception{
        builder.authenticationProvider(provider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/login*", "/register*", "/h2-console/**");
    }

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

    /* Unused in favour of token provider
    public DaoAuthenticationProvider authProvider(PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(this.userDetailsService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }
    */

    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    AbstractAuthenticationProcessingFilter createTokenFilter() throws Exception {
        TokenAuthenticationFilter filter = new TokenAuthenticationFilter(REQUEST_MATCHER);
        filter.setAuthenticationManager(authenticationManager());

        return filter;
    }
}
