package com.cactus.systems;

import com.cactus.adapters.AuthAdapter;
import com.cactus.entities.User;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

/***
 * Represents the system that controls users
 */
@Singleton
public class UserSystem {

    private final AuthAdapter authAdapter;
    User currentUser;

    /***
     * Create a new GroceryListSystem with user and groceryList managers, and mapping of grocery list name
     */
    @Inject
    public UserSystem(AuthAdapter authAdapter) {
        this.authAdapter = authAdapter;
    }

    /**
     * Get the current user's token
     *
     * @return token of the current user
     */
    public String getToken() {
        return currentUser.getToken();
    }

    /***
     * Given a name and a password, creates a new user and stores the user as current.
     * It will return false when .create() returns no user
     * telling us that the name was already taken or password was not of the correct form.
     *
     * @param name     a String containing the name of the new user
     * @param username a String containing the username of the new user
     * @param password a String containing the password of the new user
     * @return true if a newUser was created
     */
    public boolean createUser(String name, String username, String password) throws InvalidParamException, ServerException {
        if (name.trim().isEmpty() || name.trim().isEmpty() || name.trim().isEmpty() ){
            throw new InvalidParamException("Fields cannot be blank",
                    "Blank fields. Input was \nName = \"" + name + "\"\nUsername = \"" + username + "\"\nPassword = \"" + password + "\"");
        }
        return updateCurrentUser(this.authAdapter.create(name, username, password));
    }

    /***
     * Given a username and a password, logs in a user and stores the user as current.
     * It will return false when .authenticate() returns a Response with Status that is not "OK"
     * telling us that the username and password were not a correct login pair.
     *
     * @param username the username of the user logging in
     * @param password the password of the user logging in
     * @return true if login was successful, false otherwise
     */
    public boolean login(String username, String password) throws InvalidParamException, ServerException {
        return updateCurrentUser(this.authAdapter.login(username, password));
    }

    /***
     * checks whether user exists and if so makes that user
     * the current user and returns true
     *
     * @param user the user that is being logged in
     * @return true if user exists
     */
    private boolean updateCurrentUser(User user) {
        if (user != null) {
            this.currentUser = user;
            return true;
        }

        return false;
    }

    /***
     * Logout the user by setting the user id to -1
     *
     * @return true if there existed a valid user id before exiting
     */
    public void logout() throws InvalidParamException, ServerException {
        if (this.currentUser != null) {

            authAdapter.logout(this.getToken());

            this.currentUser = null;
        }
    }

    /**
     * Return the current user's username so that UI can display it.
     *
     * @return username of user
     */
    public String getUsername() {
        return this.currentUser.getUsername();
    }

    /**
     * Return the current user's name so that UI can display it.
     *
     * @return name of user
     */
    public String getName() {
        return this.currentUser.getName();
    }

    /**
     * Edit a user's details to be the provided strings. Only the name and password
     * of a user can be set. If a password of an empty string is provided, it is assumed that
     * the password remains unchanged.
     * <p>
     * This method checks for valid inputs. If the provided name and password do not contain at least
     * one non-whitespace character, then an InvalidParamException is thrown.
     *
     * @param name     the String name to set
     * @param password the String password to set
     * @throws InvalidParamException if the name or password do not contain at least one non-whitespace
     *                               character, assuming the password is changing
     * @throws ServerException if something went wrong during the request
     */
    public void editUser(String name, String password) throws InvalidParamException, ServerException {
        if ((!password.isEmpty() && password.trim().isEmpty()) || name.trim().isEmpty()) {
            throw new InvalidParamException("Name/password cannot be blank",
                    "Blank username/password. Input was \nName = \"" + name + "\"\nPassword = \"" + password + "\"");
        }

        if (password.isEmpty()) {
            password = null;
        }

        this.currentUser = this.authAdapter.editUserDetails(name, password, this.getToken());
    }

    /**
     * Get all the usernames of the friends of the current user as a list
     *
     * @return the usernames of the friends of the current user
     */
    public List<String> getFriends() {
        return this.currentUser.getFriends();
    }

    /**
     * Adds the given user as a friend to the current user
     * @param username username of the user to be added as a friend
     * @throws InvalidParamException
     * @throws ServerException
     */
    public void addFriend(String username) throws InvalidParamException, ServerException {
        this.currentUser = this.authAdapter.addFriend(username, this.getToken());
    }

}

