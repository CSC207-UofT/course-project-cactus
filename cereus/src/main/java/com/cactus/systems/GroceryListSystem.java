package com.cactus.systems;

import com.cactus.adapters.*;
import com.cactus.adapters.Response;
import com.cactus.adapters.Response.Status;
import com.cactus.entities.GroceryItem;
import com.cactus.entities.GroceryList;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/***
 * Represents the system that controls grocery lists and grocery items
 */
@Singleton
public class GroceryListSystem {

    private final GroceryAdapter groceryAdapter;
    private long currentGroceryListId;
    private HashMap<String, Long> currentListMap;
    private HashMap<String, Long> currentItemMap;

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
     * @return true if a new groceryList was created, false otherwise
     */
    public boolean newGroceryList(String name, long userid) {
        GroceryList newGroceryList = this.groceryAdapter.createGroceryList(name, userid);

        if (newGroceryList != null) {
            this.currentGroceryListId = newGroceryList.getId();
            this.currentListMap.put(name, newGroceryList.getId());
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
        this.currentListMap = new HashMap<>();
        ArrayList<String> listNames = new ArrayList<>();
        List<GroceryList> groceryLists =
                this.groceryAdapter.getGroceryListsByUser(userid);

        groceryLists = new ArrayList<GroceryList>();
        groceryLists.add(new GroceryList("List 1"));
        groceryLists.add(new GroceryList("List 2"));
        groceryLists.add(new GroceryList("List 3"));

        for(GroceryList groceryList : groceryLists){
            this.currentListMap.put(groceryList.getName(), groceryList.getId());
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
        this.currentItemMap = new HashMap<String, Long>();
        ArrayList<String> itemNames = new ArrayList<>();
        List<GroceryItem> groceryItems =
                this.groceryAdapter.getGroceryItems(this.currentGroceryListId, userid);

        groceryItems = new ArrayList<GroceryItem>();
        groceryItems.add(new GroceryItem("Apple", 1));
        groceryItems.add(new GroceryItem("Banana", 1));
        groceryItems.add(new GroceryItem("Melon", 1));

        for(GroceryItem groceryItem : groceryItems){
            this.currentItemMap.put(groceryItem.getName(), groceryItem.getId());
            itemNames.add(groceryItem.getName());
        }

        return itemNames;
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
     * @param itemName a List of strings representing the items to the added to the grocery list
     * @param userid id for the current user
     * @return true if items were added, false otherwise
     */
    public boolean addGroceryItem(String itemName, long userid) {
        GroceryItem newItem = this.groceryAdapter.setGroceryItem(itemName, this.currentGroceryListId, userid);

        if(newItem != null){
            this.currentListMap.put(itemName, newItem.getId());
            return true;
        } else {
            return false;
        }
    }


    /***
     * Delete the current list
     *
     * @param userid the id of the user that the to be deleted list belongs to
     * @return true if the list was successfully deleted and false if list DNE
     */
    public boolean deleteGroceryList(long userid, String listName) {
        return this.groceryAdapter.deleteGroceryList(this.currentListMap.get(listName), userid);
    }

    /***
     * delete a grocery item
     *
     * @param userid id of the user to which item belongs
     * @param itemName name of the item to be deleted
     * @return true if deletion was successful
     */
    public boolean deleteGroceryItem(long userid, String itemName){
        return this.groceryAdapter.deleteGroceryItem(this.currentItemMap.get(itemName), this.currentGroceryListId, userid);
    }

    /***
     * Return the current list name so that UI can display it.
     *
     * @return name of grocery list
     * */
    public String getListName(long userid) {
        return this.groceryAdapter.getGroceryList(this.currentGroceryListId, userid).getName();
    }

    /***
     * Set the current grocery list using a string
     *
     * @param listName name of the list to be set as current
     */
    public void setCurrentGroceryList(String listName){
        this.currentGroceryListId = this.currentListMap.get(listName);
    }

}

