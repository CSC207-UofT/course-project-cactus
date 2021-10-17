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

    private GroceryAdapter groceryListManager;
    long currentGroceryListId;

    private List<String> currentItems;

    /***
     * Create a new GroceryListSystem with groceryList managers
     */
    public GroceryListSystem(GroceryAdapter groceryListManager){
        this.groceryListManager = groceryListManager;
    }

    /***
     * Given a name from UI, creates a new GroceryList.
     * It will return false when .createGroceryList() returns a Response with Status that is not "OK"
     * telling us that the name was already taken
     *
     * @param name
     * @return true if a new groceryList was created, false otherwise
     */
    public boolean newGroceryList(String name, long userid){
        Response groceryListResponse = this.groceryListManager.createGroceryList(name, userid);

        if (groceryListResponse.getStatusCode() == Status.OK) {
            this.currentGroceryListId = Long.parseLong(groceryListResponse.getPayload().get("listid"));
            this.currentItems = new ArrayList<String>();
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
    public HashMap<Long, String> getGroceryListNames(long userid){
        HashMap<Long, String> groceryListNameMap = new HashMap<Long, String>();
        HashMap<String, String> groceryListsPayload =
                this.groceryListManager.getGroceryListsByUser(userid).getPayload();

        for(int i = 0; i < Integer.parseInt(groceryListsPayload.get("length")); i++){
            long listId = Integer.parseInt(groceryListsPayload.get(i));
            String listName = this.groceryListManager.getGroceryList(listId, userid).getPayload().get("name");
            groceryListNameMap.put(listId, listName);
        }

        return groceryListNameMap;
    }

    /***
     * Return the list of grocery list item names for the current grocery list
     * so that UI can display them to the user.
     *
     * @return groceryItemNames
     * */
    public ArrayList<String> getGroceryItemNames(long userid){
        ArrayList<String> groceryItemNames = new ArrayList<String>();
        HashMap<String, String> groceryListPayload =
                this.groceryListManager.getGroceryList(this.currentGroceryListId, userid).getPayload();

        for(int i = 0; i < Integer.parseInt(groceryListPayload.get("length")); i++){
            String itemName = groceryListPayload.get(i);
            groceryItemNames.add(itemName);
        }

        return groceryItemNames;
    }

    /***
     * Exit the list by setting the list id to -1
     *
     * @return true if there existed a valid list id before exiting
     */
    public boolean exitGroceryList(){
        if (this.currentGroceryListId != -1){
            this.currentGroceryListId = -1;
            return true;
        } else{
            return false;
        }
    }

    /***
     * Add grocery items to the current grocery list
     *
     * @param item a String representing the item to the add to the grocery list
     * @param userid id for the current user
     * @return true if items were added, false otherwise
     */
    public boolean addGroceryItems(String item, long userid){
        this.currentItems.add(item);

        Response setGroceryItemResponse =
                this.groceryListManager.setGroceryItems(this.currentItems, this.currentGroceryListId, userid);

        return setGroceryItemResponse.getStatusCode() == Status.OK;
    }



    /***
     * Delete the current list
     *
     * @param userid
     * @return true if the list was successfully deleted and false if list DNE
     */
    public boolean deleteGroceryList(long userid){
        long toBeDeletedListId = this.currentGroceryListId;

        if(exitGroceryList()){
            Response deleteListResponse = this.groceryListManager.deleteGroceryList(toBeDeletedListId, userid);

            return deleteListResponse.getStatusCode() == Status.OK;
        }

        return false;
    }


}

