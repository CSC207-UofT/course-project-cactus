package com.cactus.systems;

import com.cactus.managers.UserManager;
import com.cactus.data.EntityRepository;
import com.cactus.entities.GroceryItem;
import com.cactus.entities.GroceryList;
import com.cactus.entities.User;
import com.cactus.managers.GroceryListManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/***
 * Represents the system that controls users, grocery lists, and grocery list items
 */
public class GroceryListSystem {

    private EntityRepository repository;
    private UserManager userManager;
    private GroceryListManager groceryListManager;
    private User currentUser;
    private GroceryList currentGroceryList;

    /***
     * Create a new GroceryListSystem with user and groceryList managers, and mapping of grocery list name
     */
    public GroceryListSystem(){
        repository = new EntityRepository();
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
            return this.repository.saveUser(newUser);
        } else{
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
        } else{
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
        GroceryList newGroceryList = this.groceryListManager.createGroceryList(name, this.currentUser.getId());

        if (!Objects.isNull(newGroceryList)){
            this.currentGroceryList = newGroceryList;
            return this.repository.saveGroceryList(newGroceryList);
        } else {
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
    public boolean newItem(String name){
        GroceryItem newGroceryItem = this.groceryListManager.addItemToGroceryList(name, this.currentGroceryList.getId());
        if(!Objects.isNull(newGroceryItem)){
            return this.repository.saveGroceryItem(newGroceryItem);
        } else {
            return false;
        }
    }


    /***
     * Return a mapping of all the groceryLists from their name to their ID
     * so that UI can print the names and tell the controller which ID was picked
     *
     * @return groceryListNameMap
     */
    public HashMap<String, Long> getGroceryListNames(){
        HashMap<String, Long> groceryListNameMap = new HashMap<String, Long>();

        for(GroceryList groceryList : this.repository.getGroceryListByUser(this.currentUser)){
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
        ArrayList<String> groceryItemNames = new ArrayList<String>();

        for(GroceryItem groceryItem : this.repository.getGroceryItemsByList(this.currentGroceryList)){
            groceryItemNames.add(groceryItem.getName());
        }

        return groceryItemNames;
    }

}
