package com.saguaro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Saguaro
 *
 * @author Charles Wong
 */
@SpringBootApplication
public class SaguaroApplication {

    /**
     * Main method for Saguaro. Runs this Spring application with provided arguments.
     *
     * @param args an array of String command line arguments passed when running this application
     */
    public static void main(String[] args) {
        SpringApplication.run(SaguaroApplication.class, args);
    }

}
