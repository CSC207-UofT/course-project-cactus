package com.cactus.systems;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
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

    private String getToken() {
        return this.userSystem.getToken();
    }

    /**
     * Create user using UserSystem's createUser method
     *
     * @param name     the name of the user that is being created
     * @param username the username of the user that is being created
     * @param password the password of the user that is being created
     * @return true if user was successfully created
     */
    public boolean createUser(String name, String username, String password) {
        return this.userSystem.createUser(name, username, password);
    }

    /**
     * Login a user using UserSystem's login method
     *
     * @param username the username of the user being logged in
     * @param password the password of the user being logged in
     * @return true if user was successfully logged in
     */
    public boolean login(String username, String password) {
        return this.userSystem.login(username, password);
    }

    /**
     * Logout a user using the UserSystem's logout method
     *
     * @return true if user was successfully logged out
     */
    public boolean logout() {
        return this.userSystem.logout();
    }

    /**
     * Get username of current user using UserSystem's getUserName method
     *
     * @return username of current user
     */
    public String getUserName() {
        return this.userSystem.getUserName();
    }

    // GroceryListSystem methods

    /**
     * Create a new grocery list with the given name. Additionally specify if this list should be
     * a template.
     *
     * @param name     the String name of the grocery list that is to be created
     * @param template a boolean specifying if the created list should be a template
     * @return true if grocery list was successfully created
     */
    public boolean newGroceryList(String name, boolean template) {
        return this.groceryListSystem.newGroceryList(name, this.getToken(), template, null);
    }

    /**
     * Create a new grocery list with the given name, and initialize it with a template, specified by name.
     * This implies that the new grocery list is not a template.
     *
     * @param name         the String to initialize the name of the new grocery list to
     * @param templateName the String name of the template to initialize the new grocery list with
     * @return true if the specified grocery list was successfully created, false otherwise
     */
    public boolean newGroceryListWithTemplate(String name, String templateName) {
        return this.groceryListSystem.newGroceryList(name, this.getToken(), false, templateName);
    }

    /**
     * Get grocery list names using GroceryListSystem's getGroceryListNames method
     *
     * @return a list of grocery list names belonging to the current user
     */
    public List<String> getGroceryListNames() {
        return this.groceryListSystem.getGroceryListNames(this.getToken(), false);
    }

    /**
     * Get grocery template names. Delegates to GroceryListSystem.
     *
     * @return a list of grocery template names belonging to the current user
     */
    public List<String> getGroceryTemplateNames() {
        return this.groceryListSystem.getGroceryListNames(this.getToken(), true);
    }

    /**
     * Get grocery item names using GroceryListSystem's getGroceryItemNames method
     *
     * @return a list of grocery item names belonging to the current list
     */
    public ArrayList<String> getGroceryItemNames() {
        return this.groceryListSystem.getGroceryItemNames(this.getToken());
    }

    /**
     * Add grocery items to the current grocery list
     *
     * @param items list of names of the items that are to be added to the current grocery list
     * @return true of item was successfully added
     */
    public boolean addGroceryItems(List<String> items) {
        return this.groceryListSystem.addGroceryItems(items, this.getToken());
    }

    /**
     * Delete the current grocery list
     *
     * @return true if grocery list was successfully deleted
     */
    public boolean deleteGroceryList(String listName) {
        return this.groceryListSystem.deleteGroceryList(this.getToken(), listName);
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
}
