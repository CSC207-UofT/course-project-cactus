package com.cactus;

import java.util.HashMap;
import java.util.Objects;

/***
 * Represents the system that controls users, grocery lists, and grocery list items
 */
public class GroceryListSystem {

    private UserManager userManager;
    private GroceryListManager groceryListManager;
    private User currentUser;
    private GroceryList currentGroceryList;
    private HashMap<Integer, String> groceryListNames;

    /***
     * Create a new GroceryListSystem with user and groceryList managers, and mapping of grocery list name
     */
    public GroceryListSystem(){
        groceryListManager = new GroceryListManager();
        userManager = new UserManager();
        groceryListNames = new HashMap<Integer, String>();
    }

    /***
     * Given a name and a password, creates a new user and stores the user as current.
     * It will return false when the .create() function returns null
     * telling us that the name was already taken or password was not of the correct form.
     *
     * @param name
     * @param password
     * @return true if a newUser was created
     */
    public boolean createUser(String name, String username, String password){
        User newUser = userManager.addUser(name, username, password);

        if (!Objects.isNull(newUser)){
            currentUser = newUser;
            return true;
        }
        else{
            return false;
        }
    }

    /***
     * Deletes a given user with username and password.
     * It will return false when the .deleteUser() function returns false
     * telling us that the username and password could not be matched to
     * an existing user to be deleted.
     *
     * @param username
     * @param password
     * @return true if user was deleted, false otherwise
     */
    public boolean deleteUser(String username, String password){
        return userManager.deleteUser(username, password);
    }

    /***
     * Given a name and a password, logs in a user and stores the user as current.
     * It will return false when the .authenticate() function returns null
     * telling us that the name and password were not a correct login pair.
     *
     * @param name
     * @param password
     * @return true if login was successful, false otherwise
     */
    public boolean login(String name, String password){
        User newUser = this.userManager.authenticate(name, password);

        if (!Objects.isNull(newUser)){
            this.currentUser = newUser;
            return true;
        }
        else{
            return false;
        }
    }

    /***
     * Given a name from UI, creates a new GroceryList.
     * It will return false when the .createList() function returns a null
     * telling us that the name was already taken
     *
     * @param name
     * @return true if a new groceryList was created, false otherwise
     */
    public boolean newGroceryList(String name){
        GroceryList newGroceryList = this.groceryListManager.createList(name);

        if (!Objects.isNull(newGroceryList)){
            this.currentGroceryList = newGroceryList;
            this.groceryListNames.put(newGroceryList.getId(), newGroceryList.getName());
            return true;
        }
        else{
            return false;
        }
    }

    /***
     * Given a category and item name, creates a new item for the current grocery list.
     * It will return false when the .createItem() function returns a null
     * telling us that the name was already taken
     *
     * @param category
     * @param name
     * @return true if a new grocery list item was created, false otherwise
     */
    public boolean newItem(String category, String name){
        GroceryListItem newGroceryListItem = this.groceryListManager.createItem(category, name);
        return !Objects.isNull(newGroceryListItem);
    }


    /***
     * Return a mapping of all the groceryLists from their ID to their names
     * so that UI can print the names and tell the controller which ID was picked
     *
     * @return groceryListNames
     */
    public HashMap<Integer, String> getGroceryListNames(){

        return this.groceryListNames;
    }

}
