package com.cactus.systems;

import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/***
 * Facade controller in charge of calling UserSystem and GroceryListSystem methods
 */
@Singleton
public class UserInteractFacade {
    private final UserSystem userSystem;

    private final GroceryListSystem groceryListSystem;

    /**
     * Creates a new UserInteractFacade object
     */
    @Inject
    public UserInteractFacade(UserSystem userSystem, GroceryListSystem groceryListSystem) {
        this.userSystem = userSystem;
        this.groceryListSystem = groceryListSystem;
    }

    // UserSystem methods

    /**
     * Getter for the current user's token
     *
     * @return string representation of the user's token
     */
    private String getToken() {
        return this.userSystem.getToken();
    }

    /**
     * Create user using UserSystem's createUser method
     *
     * @param name     the name of the user that is being created
     * @param username the username of the user that is being created
     * @param password the password of the user that is being created
     */
    public void createUser(String name, String username, String password) throws InvalidParamException, ServerException {
        this.userSystem.createUser(name, username, password);
    }

    /**
     * Login a user using UserSystem's login method
     *
     * @param username the username of the user being logged in
     * @param password the password of the user being logged in
     */
    public void login(String username, String password) throws InvalidParamException, ServerException {
        this.userSystem.login(username, password);
    }

    /**
     * Logout a user using the UserSystem's logout method
     */
    public void logout() throws InvalidParamException, ServerException {
        this.userSystem.logout();
        this.groceryListSystem.clearData();
    }

    /***
     * Return the current user's username so that UI can display it.
     *
     * @return username of user
     * */
    public String getUsername() {
        return this.userSystem.getUsername();
    }

    /**
     * Get username of current user using UserSystem's getUserName method
     *
     * @return username of current user
     */
    public String getName() {
        return this.userSystem.getName();
    }

    /**
     * Edit a user's details to be the provided strings. Only the name and password
     * of a user can be set. If a password of an empty string is provided, it is assumed that
     * the password remains unchanged. The submitted strings cannot be blank.
     *
     * @param name     the String name to change to
     * @param password the String password to change to
     * @throws InvalidParamException if the provided strings are invalid edits
     */
    public void editUserDetails(String name, String password) throws InvalidParamException, ServerException {
        this.userSystem.editUser(name, password);
    }

    /**
     * Get all the usernames of the friends of the current user as a list
     *
     * @return the usernames of the friends of the current user
     */
    public List<String> getFriends() {
        return this.userSystem.getFriends();
    }

    public void addFriend(String username) throws InvalidParamException, ServerException {
        this.userSystem.addFriend(username);
    }

    // GroceryListSystem methods

    /**
     * Create a new grocery list with the given name. Additionally specify if this list should be
     * a template.
     *
     * @param name     the String name of the grocery list that is to be created
     * @param template a boolean specifying if the created list should be a template
     */
    public void newGroceryList(String name, boolean template) throws InvalidParamException, ServerException {
        this.groceryListSystem.newGroceryList(name, this.getToken(), template, null);
    }

    /**
     * Create a new grocery list with the given name, and initialize it with a template, specified by name.
     * This implies that the new grocery list is not a template.
     *
     * @param name         the String to initialize the name of the new grocery list to
     * @param templateName the String name of the template to initialize the new grocery list with
     */
    public void newGroceryListWithTemplate(String name, String templateName) throws InvalidParamException, ServerException {
        this.groceryListSystem.newGroceryList(name, this.getToken(), false, templateName);
    }


    /**
     * Get grocery template names. Delegates to GroceryListSystem.
     *
     * @return a list of grocery template names belonging to the current user
     */
    public List<String> getGroceryTemplateNames() throws InvalidParamException, ServerException {
        return this.groceryListSystem.getGroceryListNames(this.getToken(), true);
    }

    /**
     * Get grocery list names. Delegates to GroceryListSystem.
     * Uses a force parameter that will reset the mapping in the GroceryListSystem.
     *
     * @return a list of grocery list names belonging to the current user
     * @throws InvalidParamException can be called if parameters fail
     * @throws ServerException       can be called if the server fails
     */
    public List<String> getGroceryListNamesForce() throws InvalidParamException, ServerException {
        return this.groceryListSystem.getGroceryListNames(this.getToken(), false, true);
    }

    /**
     * Get grocery template names. Delegates to GroceryListSystem.
     * Uses a force parameter that will reset the mapping in the GroceryListSystem.
     *
     * @return a list of grocery template names belonging to the current user
     * @throws InvalidParamException can be called if parameters fail
     * @throws ServerException       can be called if the server fails
     */
    public List<String> getGroceryTemplateNamesForce() throws InvalidParamException, ServerException {
        return this.groceryListSystem.getGroceryListNames(this.getToken(), true, true);
    }

    /**
     * Get grocery item names using GroceryListSystem's getGroceryItemNames method
     *
     * @return a list of grocery item names belonging to the current list
     */
    public List<String> getGroceryItemNames() throws InvalidParamException, ServerException {
        return this.groceryListSystem.getGroceryCurrentList(this.getToken()).getItems();
    }

    /**
     * Get the list of users that share the current list using GroceryListSystem
     *
     * @return a list of users that share the current list
     * @throws InvalidParamException can be called if parameters fail
     * @throws ServerException       can be called if the server fails
     */
    public List<String> getGroceryListSharedUsers() throws InvalidParamException, ServerException {
        return this.groceryListSystem.getGroceryCurrentList(this.getToken()).getSharedUsers();
    }

    /**
     * Return the username of the owner of the current grocery list
     * so that UI can display them to the user.
     *
     * @return The Username of the Owner of the Current GroceryList
     */
    public String getGroceryListOwnerUserName() throws InvalidParamException, ServerException {
        return this.groceryListSystem.getGroceryCurrentListOwner(this.getToken());
    }

    /**
     * Add grocery items to the current grocery list
     *
     * @param items list of names of the items that are to be added to the current grocery list
     */
    public void addGroceryItems(List<String> items) throws InvalidParamException, ServerException {
        this.groceryListSystem.addGroceryItems(items, this.getToken());
    }

    /**
     * Delete the current grocery list
     */
    public void deleteGroceryList(String listName) throws InvalidParamException, ServerException {
        this.groceryListSystem.deleteGroceryList(this.getToken(), listName);
    }

    /**
     * Get the current grocery list's name
     *
     * @return Name of the current grocery list
     */
    public String getListName() {
        return this.groceryListSystem.getListName();
    }

    /***
     * Set the current grocery list to the given one
     *
     * @param listName name of the to be set grocery list
     */
    public void setCurrentGroceryList(String listName) {
        this.groceryListSystem.setCurrentGroceryList(listName);
    }

    /**
     * Share the current with the given user
     *
     * @param username username of the user to share with
     * @throws InvalidParamException can be called if parameters fail
     * @throws ServerException       can be called if the server fails
     */
    public void shareList(String username) throws InvalidParamException, ServerException {
        this.groceryListSystem.shareList(username, this.getToken());
    }

    /**
     * Unshare the current list with the given user
     *
     * @param username username of the user to unshare with
     * @throws InvalidParamException can be called if parameters fail
     * @throws ServerException       can be called if the server fails
     */
    public void unshareList(String username) throws InvalidParamException, ServerException {
        this.groceryListSystem.unshareList(username, this.getToken());
    }
}
