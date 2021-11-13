package com.cactus.systems;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

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

    private long getCurrentUserId(){
        return this.userSystem.getCurrentUserId();
    }

    /**
     * Create user using UserSystem's createUser method
     *
     * @param name the name of the user that is being created
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
     * Create GroceryList using GroceryListSystem's newGroceryList method
     *
     * @param name name of the grocery list that is to be created
     * @return true if grocery list was successfully created
     */
    public boolean newGroceryList(String name) {
        return this.groceryListSystem.newGroceryList(name, this.getCurrentUserId());
    }

    /**
     * Get grocery list names using GroceryListSystem's getGroceryListNames method
     *
     * @return a list of grocery list names belonging to the current user
     */
    public ArrayList<String> getGroceryListNames() {
        return this.groceryListSystem.getGroceryListNames(this.getCurrentUserId());
    }

    /**
     * Get grocery item names using GroceryListSystem's getGroceryItemNames method
     *
     * @return a list of grocery item names belonging to the current list
     */
    public ArrayList<String> getGroceryItemNames() {
        return this.groceryListSystem.getGroceryItemNames(this.getCurrentUserId());
    }

    // TODO: figure out why there are two methods for exiting
    /**
     * Exit the current grocery list using GroceryListSystem's exitGroceryList method
     * with parameters
     *
     * @param items list of items that are to be added to the current list
     * @return true if list was successfully exited and the items were added to the list
     */
    public boolean exitGroceryList(List<String> items){
        return this.groceryListSystem.exitGroceryList(items, this.getCurrentUserId());
    }

    /**
     * Exit the current grocery list using GroceryListSystem's exitGroceryList method
     *
     * @return true if the list was successfully exited
     */
    public boolean exitGroceryList() {
        return this.groceryListSystem.exitGroceryList();
    }

    /**
     * Add grocery item to the current grocery list
     *
     * @param item name of the item that is to be added to the current grocery list
     * @return true of item was successfully added
     */
    public boolean addGroceryItem(String item) {
        return this.groceryListSystem.addGroceryItem(item, this.getCurrentUserId());
    }

    /**
     * Add grocery items to the current grocery list
     *
     * @param items list of names of the items that are to be added to the current grocery list
     * @return true of item was successfully added
     */
    public boolean addGroceryItems(List<String> items){
        return this.groceryListSystem.addGroceryItems(items, this.getCurrentUserId());
    }

    // TODO: make this method require a grocery list id
    /**
     * Delete the current grocery list
     *
     * @return true if grocery list was successfully deleted
     */
    public boolean deleteGroceryList(String listName) {
        return this.groceryListSystem.deleteGroceryList(this.getCurrentUserId(), listName);
    }

    /**
     * Get the current grocery list's name
     *
     * @return Name of the current grocery list
     */
    public String getListName() {
        return this.groceryListSystem.getListName(getCurrentUserId());
    }

    public void setCurrentGroceryList(String listName){
        this.groceryListSystem.setCurrentGroceryList(listName);
    }

}
