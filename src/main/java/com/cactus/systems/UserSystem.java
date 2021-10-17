package com.cactus.systems;

import com.cactus.adapters.AuthAdapter;
import com.cactus.adapters.Response;
import com.cactus.adapters.Response.Status;

/***
 * Represents the system that controls users
 */
public class UserSystem {

    private AuthAdapter authAdapter;
    long currentUserId;

    /***
     * Create a new GroceryListSystem with user and groceryList managers, and mapping of grocery list name
     */
    public UserSystem(AuthAdapter authAdapter){
        this.authAdapter = authAdapter;
    }

    // TODO: unCLEAN
    public long getCurrentUserId() {
        return currentUserId;
    }

    /***
     * Given a name and a password, creates a new user and stores the user as current.
     * It will return false when .create() returns a Response with Status that is not "OK"
     * telling us that the name was already taken or password was not of the correct form.
     *
     * @param name     a String containing the name of the new user
     * @param username a String containing the username of the new user
     * @param password a String containing the password of the new user
     * @return true if a newUser was created
     */
    public boolean createUser(String name, String username, String password) {
        Response userResponse = this.authAdapter.create(name, username, password);

        if (userResponse.getStatusCode() == Status.OK){
            this.currentUserId = Long.parseLong(userResponse.getPayload().get("userid"));
            return true;
        }

        return false;
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
    public boolean login(String username, String password){
        Response userResponse = this.authAdapter.login(username, password);

        if (userResponse.getStatusCode() == Status.OK){
            this.currentUserId = Long.parseLong(userResponse.getPayload().get("userid"));
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
        if (this.currentUserId != -1){
            this.currentUserId = -1;
            return true;
        } else{
            return false;
        }
    }

}

