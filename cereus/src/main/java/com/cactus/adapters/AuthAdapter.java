package com.cactus.adapters;
import com.cactus.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.net.URISyntaxException;

public interface AuthAdapter {

    /**
     * Returns a Response object with the results of a login operation done using the
     * provided username and password.
     *
     * If the login was successful, the Response will have code 200 (OK), and a
     * payload with the following entries:
     * - userid: a string representation of the user' ID (long)
     * - name: a string representation of the user's name (string)
     *
     * The type in parentheses denotes the value's original type.
     *
     * If the login was unsuccessful, the Response will have code 400 (BAD_REQUEST)
     * and a null payload.
     *
     * @param username a String containing the username of the user to be logged in
     * @param password a String containing the password to validate
     * @return         a Response to the login operation
     *
     * @see Response
     */
    User login(String username, String password) throws IOException, InterruptedException, URISyntaxException;

    /**
     * Returns a Response object with the results of a create user operation done
     * using the provided username, password, and name.
     *
     * If the user was created successfully, the Response will have code 200 (OK),
     * and a payload with the following keys:
     * - userid: a string representation of the user' ID (long)
     * - name: a string representation of the user's name (string)
     *
     * The type in parentheses denotes the value's original type.
     *
     * If the user creation was unsuccessful (i.e. username already exists), the
     * Response will have code 400 (BAD_REQUEST) and a null payload.
     *
     * @param username a String containing the username of the new user
     * @param password a String containing the password of the new user
     * @param name     a String containing the name of the user
     * @return         a Response to the create user operation
     *
     * @see Response
     */
    User create(String username, String password, String name) throws IOException, URISyntaxException, InterruptedException;

    boolean logout(String token) throws IOException, InterruptedException, URISyntaxException;
}
