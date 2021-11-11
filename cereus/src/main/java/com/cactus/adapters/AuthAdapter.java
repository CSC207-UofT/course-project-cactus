package com.cactus.adapters;
import com.cactus.entities.User;

import java.io.IOException;
import java.net.URISyntaxException;


public interface AuthAdapter {

    /**
     * Returns a User object that corresponds to the provided username and password.
     *
     * If the username and password do not correspond to an existing User,
     * then null is returned, and no login takes place.
     *
     * @param username a String containing the username of the user to be logged in
     * @param password a String containing the password to validate
     * @return         a User object that corresponds to the given parameters
     *
     * @see User
     */
    User login(String username, String password);


    /**
     * Returns a User object created with the provided username, password, and name.
     *
     * Note: the resulting user object is also logged in as the current user, and no further call to log in the User
     * is required
     *
     * If the user has the same username and password (but different name)
     * as one that already exists, then that person is logged in and no creation takes place.
     *
     * If the user has the same username and different password as an existing user,
     * then null is returned and no creation or login takes place.
     *
     * If the username has a different username, then a User object corresponding to the given information is created,
     * and returned
     *
     * @param username a String containing the username of the new user
     * @param password a String containing the password of the new user
     * @param name     a String containing the name of the user
     * @return         a User object created with the given parameters
     *
     * @see User
     */
    User create(String username, String password, String name);


    /**
     * Returns whether the User object with the corresponding token is successfully logged out of the application.
     *
     *
     * @param token    a String containing a token unique to every User, stored in the User class
     * @return         whether the User corresponding to the token is logged out or not
     *
     * @see User
     */
    boolean logout(String token);
}
