package com.cactus.adapters;

import com.cactus.entities.User;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;

public interface AuthAdapter {

    /**
     * Returns a User object that corresponds to the provided username and password.
     * <p>
     * If the username and password do not correspond to an existing User,
     * then null is returned, and no login takes place.
     *
     * @param username a String containing the username of the user to be logged in
     * @param password a String containing the password to validate
     * @return a User object that corresponds to the given parameters
     * @see User
     */
    User login(String username, String password);


    /**
     * Returns a User object created with the provided username, password, and name.
     * <p>
     * If the user has the same username as one that already exists, then null is returned and no creation takes place.
     * <p>
     * If the username has a different username, then a User object corresponding to the given information is created,
     * and returned
     *
     * @param name     a String containing the name of the user
     * @param username a String containing the username of the new user
     * @param password a String containing the password of the new user
     * @return a User object created with the given parameters
     * @see User
     */
    User create(String name, String username, String password);


    /**
     * Returns whether the User object with the corresponding token is successfully logged out of the application.
     *
     * @param token a String containing a token unique to every User, stored in the User class
     * @return whether the User corresponding to the token is logged out or not
     * @see User
     */
    boolean logout(String token);

    /**
     * Edit a user's details. If null is provided for the password, then the password
     * remains unchanged.
     * <p>
     * On server error, throws a ServerException. This method can also throw a InvalidParamException,
     * but the caller is expected to sanitize inputs as much as possible.
     *
     * @param name     the String name to set
     * @param password the String password to set
     * @param token    a String authentication token
     * @return the newly edited User object
     * @throws InvalidParamException if invalid parameters were provided
     * @throws ServerException if the server response contains a 5xx response code
     */
    User editUserDetails(String name, String password, String token) throws InvalidParamException, ServerException;
}
