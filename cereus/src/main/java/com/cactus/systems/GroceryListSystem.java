package com.cactus.systems;

import com.cactus.adapters.*;
import com.cactus.adapters.Response;
import com.cactus.adapters.Response.Status;
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

    @Inject
    GroceryAdapter groceryAdapter;

    long currentGroceryListId;
    HashMap<String, Long> currentListNamesMap;

    private List<String> currentItems;

    /***
     * Create a new GroceryListSystem with groceryList managers
     */
    @Inject
    public GroceryListSystem(){}

    /***
     * Given a name from UI, creates a new GroceryList.
     * It will return false when .createGroceryList() returns a Response with Status that is not "OK"
     * telling us that the name was already taken
     *
     * @param name given name
     * @return true if a new groceryList was created, false otherwise
     */
    public boolean newGroceryList(String name, long userid) {
        GroceryList newGroceryList = this.groceryAdapter.createGroceryList(name, userid);

        if (newGroceryList != null) {
            this.currentGroceryListId = newGroceryList.getId();
            this.currentItems = new ArrayList<>();
            return true;
        }

        return false;
    }

    /***
     * Return a mapping of all the groceryLists from their name to their ID
     * so that UI can print the names and tell the controller which ID was picked
     *
     * @return groceryListNameMap
     */
    public ArrayList<String> getGroceryListNames(long userid) {
        this.currentListNamesMap = new HashMap<>();
        ArrayList<String> listNames = new ArrayList<>();
        List<GroceryList> groceryLists =
                this.groceryAdapter.getGroceryListsByUser(userid);

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
     * @return groceryItemNames
     * */
    public ArrayList<String> getGroceryItemNames(long userid) {
        ArrayList<String> groceryItemNames = new ArrayList<>();
        List<GroceryItem> groceryItems =
                this.groceryAdapter.getGroceryItems(this.currentGroceryListId, userid);

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
     * @param userid the id of the user that is using the current list
     * @return true if there existed a valid list id before exiting
     */
    public boolean exitGroceryList(List<String> items, long userid){
        if (this.currentGroceryListId != -1){
            if(!this.addGroceryItems(items, userid)){
                return false;
            }
            this.currentGroceryListId = -1;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Exit the list by setting the list id to -1
     * No items were added so ne parameter
     *
     * @return true if successfully exited list
     */
    public boolean exitGroceryList() {
        if (this.currentGroceryListId != -1) {
            this.currentGroceryListId = -1;
            return true;
        } else {
            return false;
        }
    }

    /***
     * Add grocery items to the current grocery list
     *
     * @param item a List of strings representing the items to the added to the grocery list
     * @param userid id for the current user
     * @return true if items were added, false otherwise
     */
    public boolean addGroceryItem(String item, long userid) {
        this.currentItems.add(item);

        return this.groceryAdapter.setGroceryItems(this.currentItems, this.currentGroceryListId, userid);
    }

    public boolean addGroceryItems(List<String> items, long userid){
        return this.groceryAdapter.setGroceryItems(items, this.currentGroceryListId, userid);
    }


    /***
     * Delete the current list
     *
     * @param userid the id of the user that the to be deleted list belongs to
     * @return true if the list was successfully deleted and false if list DNE
     */
    public boolean deleteGroceryList(long userid) {
        long toBeDeletedListId = this.currentGroceryListId;

        if(exitGroceryList(new ArrayList<>(), userid)){
            return this.groceryAdapter.deleteGroceryList(toBeDeletedListId, userid);
        }

        return false;
    }

    /***
     * Return the current list name so that UI can display it.
     *
     * @return name of grocery list
     * */
    public String getListName(long userid) {
        return this.groceryAdapter.getGroceryList(this.currentGroceryListId, userid).getName();
    }

}

