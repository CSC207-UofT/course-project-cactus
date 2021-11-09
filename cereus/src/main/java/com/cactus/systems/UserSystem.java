package com.cactus.systems;

import com.cactus.adapters.AuthAdapter;
import com.cactus.entities.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;

/***
 * Represents the system that controls users
 */
@Singleton
public class UserSystem {

    @Inject
    AuthAdapter authAdapter;

    User currentUser;

    /***
     * Create a new GroceryListSystem with user and groceryList managers, and mapping of grocery list name
     */
    public UserSystem(){
    }

    /**
     * Get the current user's id
     *
     * @return id of the current user
     */
    public long getCurrentUserId() {
        return currentUser.getId();
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
    public boolean createUser(String name, String username, String password) {
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
    public boolean login(String username, String password) {
        return updateCurrentUser(this.authAdapter.login(username, password));
    }

    /***
     * checks whether user exists and if so makes that user
     * the current user and returns true
     *
     * @param user the user that is being logged in
     * @return true if user exists
     */
    private boolean updateCurrentUser(User user){
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
    public boolean logout() {
        if (this.currentUser != null) {
            this.currentUser = null;
            return true;
        } else {
            return false;
        }
    }

    /***
     * Return the current user's name so that UI can display it.
     *
     * @return name of user
     * */
    public String getUserName() {
        return this.currentUser.getName();
    }

}

