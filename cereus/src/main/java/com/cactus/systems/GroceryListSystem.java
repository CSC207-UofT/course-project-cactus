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

    private List<String> currentItems;

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
    public boolean newGroceryList(String name, String token) {
        GroceryList newGroceryList = this.groceryAdapter.createGroceryList(name, token);

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
    public ArrayList<String> getGroceryListNames(String token) {
        this.currentListNamesMap = new HashMap<>();
        ArrayList<String> listNames = new ArrayList<>();
        List<GroceryList> groceryLists =
                this.groceryAdapter.getGroceryListsByUser(token);

//        groceryLists = new ArrayList<GroceryList>();
//        groceryLists.add(new GroceryList("List 1"));
//        groceryLists.add(new GroceryList("List 2"));
//        groceryLists.add(new GroceryList("List 3"));

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
    public ArrayList<String> getGroceryItemNames(String token) {
        ArrayList<String> groceryItemNames = new ArrayList<>();
        List<GroceryItem> groceryItems =
                this.groceryAdapter.getGroceryItems(this.currentGroceryListId, token);

//        groceryItems = new ArrayList<GroceryItem>();
//        groceryItems.add(new GroceryItem("Apple", 1));
//        groceryItems.add(new GroceryItem("Banana", 1));
//        groceryItems.add(new GroceryItem("Melon", 1));

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
     * @param token token for the current user
     * @return true if items were added, false otherwise
     */
    public boolean addGroceryItem(String item, String token) {
        this.currentItems.add(item);

        return this.groceryAdapter.setGroceryItems(this.currentItems, this.currentGroceryListId, token);
    }

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
            return this.groceryAdapter.deleteGroceryList(toBeDeletedListId, token);
        }

        return false;
    }

    /***
     * Return the current list name so that UI can display it.
     *
     * @return name of grocery list
     * */
    public String getListName(String token) {
        return this.groceryAdapter.getGroceryList(this.currentGroceryListId, token).getName();
    }

    public void setCurrentGroceryList(String listName){
        this.currentGroceryListId = this.currentListNamesMap.get(listName);
    }

}

