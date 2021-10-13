package com.cactus;

import com.cactus.entities.GroceryItem;
import com.cactus.entities.GroceryList;
import com.cactus.entities.User;

import java.util.ArrayList;
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

    /***
     * Create a new GroceryListSystem with user and groceryList managers, and mapping of grocery list name
     */
    public GroceryListSystem(){
        groceryListManager = new GroceryListManager();
        userManager = new UserManager();
    }

    /***
     * Given a name and a password, creates a new user and stores the user as current.
     * It will return false when the .create() function returns null
     * telling us that the name was already taken or password was not of the correct form.
     *
     * @param name
     * @param username
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
     * Given a username and a password, logs in a user and stores the user as current.
     * It will return false when the .authenticate() function returns null
     * telling us that the username and password were not a correct login pair.
     *
     * @param username
     * @param password
     * @return true if login was successful, false otherwise
     */
    public boolean login(String username, String password){
        User newUser = this.userManager.authenticate(username, password);

        if (!Objects.isNull(newUser)){
            this.currentUser = newUser;
            return true;
        }
        else{
            return false;
        }
    }

    /***
     * Logout the user by removing the current user
     */
    public void logout(){
        this.currentUser = null;
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
        GroceryItem newGroceryItem = this.groceryListManager.createItem(category, name);
        return !Objects.isNull(newGroceryItem);
    }


    /***
     * Return a mapping of all the groceryLists from their name to their ID
     * so that UI can print the names and tell the controller which ID was picked
     *
     * @return groceryListNameMap
     */
    public HashMap<String, Integer> getGroceryListNames(){
        HashMap<String, Integer> groceryListNameMap = new HashMap<String, Integer>();

        for(GroceryList groceryList : this.groceryListManager.getGroceryLists(this.currentUser.id)){
            groceryListNameMap.put(groceryList.getName(), groceryList.getId());
        }

        return groceryListNameMap;
    }

    /***
     * Return the list of grocery list item names for the current grocery list
     * so that UI can display them to the user.
     *
     * @return groceryItemNames
     * */
    public ArrayList<String> getGroceryItemNames(){
        ArrayList<String> groceryItemNames = new ArrayList<Stirng>();

        for(GroceryItem groceryItem : this.groceryListManager.getGroceryItem(this.currentGroceryList.id)){
            groceryItemNames.add(groceryItem.getName());
        }

        return groceryItemNames;
    }

}
