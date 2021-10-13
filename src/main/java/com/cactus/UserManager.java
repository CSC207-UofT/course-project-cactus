package com.cactus;

import java.util.Objects;


/***
 * Represents UserManager class which adds, removes and authenticate users.
 */

public class UserManager {
    private ArrayList<User> usersList;

    /***
     * Creates a new UserManager.
     */

    public UserManager() {
        usersList = new ArrayList<User>();
    }

    /***
     * Returns the user with the given username and password if it exists,
     * otherwise it returns null.
     *
     *
     * @param username given username
     * @param password given password
     * @return the user with this username and password, otherwise return null.
     */

    public User authenticate(String username, String password) {
        for (User user : this.usersList) {
            if (user.username.equals(username) & user.password.equals(password)) {
                return user;
            }
        }
        return null;

    }

    /***
     * Returns a new user that is created if a user with the given username
     * does not already exist, otherwise it returns null.
     *
     *
     * @param name given name
     * @param username given username
     * @param password given password
     * @return a new user that is created if a user with the given username
     *          does not already exist, otherwise it returns null.
     */

    public User addUser(String name, String username, String password) {
        for (User user:this.usersList){
            if (user.username.equals(username)){
                return null;
            }
        }
        newUser = new User(name, username, password);
        this.usersList.add(newUser);
        // EntityRepository.saveUser(newUser);
        return newUser;

    }

    /***
     * Returns true if the user deleted successfully, otherwise it returns false
     * if a user does not exist with the given username and password.
     *
     *
     * @param username given username
     * @param password given password
     * @return Returns true if the user deleted successfully, otherwise it returns false
     *         if a user does not exist with the given username and password.
     */

    public boolean deleteUser(String username, String password) {

        User user = this.authenticate(username, password);

        if (!Objects.isNull(user)) {
            return this.usersList.remove(user);
        } else {
            return false;
        }

    }

}