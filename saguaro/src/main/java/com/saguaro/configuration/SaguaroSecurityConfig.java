package com.saguaro.configuration;

import com.saguaro.service.SaguaroUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SaguaroSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SaguaroUserDetailsService userDetailsService;

    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception{
        builder.authenticationProvider(authProvider(encoder()));
    }

    @Bean
    public DaoAuthenticationProvider authProvider(PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(this.userDetailsService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(10);
    }
}
