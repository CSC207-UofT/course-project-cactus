package com.cactus.systems;

import com.cactus.adapters.*;
import com.cactus.adapters.Response;
import com.cactus.adapters.Response.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/***
 * Represents the system that controls grocery lists and grocery items
 */
public class GroceryListSystem {

    private final GroceryAdapter groceryAdapter;
    long currentGroceryListId;
    HashMap<String, Long> currentListNamesMap;

    private List<String> currentItems;

    /***
     * Create a new GroceryListSystem with groceryList managers
     */
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
        Response groceryListResponse = this.groceryAdapter.createGroceryList(name, userid);

        if (groceryListResponse.getStatusCode() == Status.OK) {
            this.currentGroceryListId = Long.parseLong(groceryListResponse.getPayload().get("listid"));
            this.currentItems = new ArrayList<>();
            return true;
        }

        return false;
    }

    /***
     * Return a mapping of all the groceryLists from their ID to their name
     * so that UI can print the names and tell the controller which ID was picked
     *
     * @return groceryListNameMap
     */
    public ArrayList<String> getGroceryListNames(long userid) {
        this.currentListNamesMap = new HashMap<>();
        ArrayList<String> listNames = new ArrayList<>();
        HashMap<String, String> groceryListsPayload =
                this.groceryAdapter.getGroceryListsByUser(userid).getPayload();

        for(int i = 0; i < Integer.parseInt(groceryListsPayload.get("length")); i++){
            long listId = Long.parseLong(groceryListsPayload.get(Integer.toString(i)));
            String listName = this.groceryAdapter.getGroceryList(listId, userid).getPayload().get("name");
            this.currentListNamesMap.put(listName, listId);
            listNames.add(listName);
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
        HashMap<String, String> groceryListPayload =
                this.groceryAdapter.getGroceryList(this.currentGroceryListId, userid).getPayload();

        for(int i = 0; i < Integer.parseInt(groceryListPayload.get("length")); i++){
            String itemName = groceryListPayload.get(Integer.toString(i));
            groceryItemNames.add(itemName);
        }

        return groceryItemNames;
    }

    /***
     * Exit the list by setting the list id to -1
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

        Response setGroceryItemResponse =
                this.groceryAdapter.setGroceryItems(this.currentItems, this.currentGroceryListId, userid);

        return setGroceryItemResponse.getStatusCode() == Status.NO_CONTENT;
    }

    public boolean addGroceryItems(List<String> items, long userid){
        Response setGroceryItemResponse =
                this.groceryAdapter.setGroceryItems(items, this.currentGroceryListId, userid);

        return setGroceryItemResponse.getStatusCode() == Status.NO_CONTENT;
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
            Response deleteListResponse = this.groceryAdapter.deleteGroceryList(toBeDeletedListId, userid);

            return deleteListResponse.getStatusCode() == Status.OK;
        }

        return false;
    }

    /***
     * Return the current list name so that UI can display it.
     *
     * @return name of grocery list
     * */
    public String getListName() {
        // TODO return current grocery list name
        return "N/A - To be implemented";
    }

}

