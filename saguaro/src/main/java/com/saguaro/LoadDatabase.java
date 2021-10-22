package com.saguaro;

import com.saguaro.entity.User;
import com.saguaro.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {
        // repository.save saves a new com.saguaro.User instance into the repository/database
        // Everything else is just boilerplate for logging
        return args -> {
            log.info("Preloading " + repository.save(new User("Ronald Bilius Weasley", "theking", "ISleptWithPeterPettigrew")));
            log.info("Preloading " + repository.save(new User("Tom Marvolo Riddle", "thedarklord", "IAmLordVoldemort")));
        };
    }
}
