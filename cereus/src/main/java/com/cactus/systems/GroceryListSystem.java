package com.cactus.systems;

import com.cactus.adapters.*;
import com.cactus.entities.GroceryItem;
import com.cactus.entities.GroceryList;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/***
 * Represents the system that controls grocery lists and grocery items
 */
@Singleton
public class GroceryListSystem {

    private final GroceryAdapter groceryAdapter;
    long currentGroceryListId;
    HashMap<String, Long> currentListNamesMap;

    /***
     * Create a new GroceryListSystem with groceryList managers
     */
    @Inject
    public GroceryListSystem(GroceryAdapter groceryAdapter){
        this.groceryAdapter = groceryAdapter;
    }

    /***
     * Given a name from UI, creates a new GroceryList.
     * It will return false when .createGroceryList() returns a Response with Status that is not "OK"
     * telling us that the name was already taken
     *
     * @param name given name
     * @param token token of user
     * @return true if a new groceryList was created, false otherwise
     */
    public boolean newGroceryList(String name, String token) {
        if (currentListNamesMap.get(name) != null) {
            return false;
        }

        GroceryList newGroceryList = this.groceryAdapter.createGroceryList(name, token);

        if (newGroceryList != null) {
            this.currentGroceryListId = newGroceryList.getId();
            this.currentListNamesMap.put(name, this.currentGroceryListId);
            return true;
        }

        return false;
    }

    /***
     * Return a mapping of all the groceryLists from their name to their ID
     * so that UI can print the names and tell the controller which ID was picked
     *
     * @param token token of user
     * @return groceryListNameMap
     */
    public ArrayList<String> getGroceryListNames(String token) {
        this.currentListNamesMap = new HashMap<>();
        ArrayList<String> listNames = new ArrayList<>();
        List<GroceryList> groceryLists =
                this.groceryAdapter.getGroceryListsByUser(token);

        for(GroceryList groceryList : groceryLists){
            this.currentListNamesMap.put(groceryList.getName(), groceryList.getId());
            listNames.add(groceryList.getName());
        }

        return listNames;
    }

    /***
     * Return the list of grocery list item names for the current grocery list
     * so that UI can display them to the user.
     *
     * @param token token of user
     * @return groceryItemNames
     * */
    public ArrayList<String> getGroceryItemNames(String token) {
        ArrayList<String> groceryItemNames = new ArrayList<>();
        List<GroceryItem> groceryItems =
                this.groceryAdapter.getGroceryItems(this.currentGroceryListId, token);

        for(GroceryItem groceryItem : groceryItems){
            groceryItemNames.add(groceryItem.getName());
        }

        return groceryItemNames;
    }

    /***
     * Exit the list by setting the list id to -1
     * items from the  parameter are saved to the list
     *
     * @param items list of strings representing the items to be added to the grocery list
     * @param token the token of the user that is using the current list
     * @return true if there existed a valid list id before exiting
     */
    public boolean exitGroceryList(List<String> items, String token){
        if (this.currentGroceryListId != -1){
            if(!this.addGroceryItems(items, token)){
                return false;
            }
            this.currentGroceryListId = -1;
            return true;
        } else {
            return false;
        }
    }

    /***
     * Add grocery items to the current grocery list

     * @param items list of items to be added
     * @param token the token of the user that it is being added to
     * @return true of items were added successfully
     */
    public boolean addGroceryItems(List<String> items, String token){
        return this.groceryAdapter.setGroceryItems(items, this.currentGroceryListId, token);
    }


    /***
     * Delete the current list
     *
     * @param token the id of the user that the to be deleted list belongs to
     * @return true if the list was successfully deleted and false if list DNE
     */
    public boolean deleteGroceryList(String token, String listName) {
        long toBeDeletedListId = this.currentListNamesMap.get(listName);

        if(exitGroceryList(new ArrayList<>(), token)){
            this.currentListNamesMap.remove(listName);
            return this.groceryAdapter.deleteGroceryList(toBeDeletedListId, token);
        }

        return false;
    }

    /***
     * Return the current list name so that UI can display it.
     *
     * @param token token of user
     * @return name of grocery list
     * */
    public String getListName(String token) {
        return this.groceryAdapter.getGroceryList(this.currentGroceryListId, token).getName();
    }


    /***
     * Set the current grocery list to the given one
     *
     * @param listName name of the to be set grocery list
     */
    public void setCurrentGroceryList(String listName){
        this.currentGroceryListId = this.currentListNamesMap.get(listName);
    }

}

