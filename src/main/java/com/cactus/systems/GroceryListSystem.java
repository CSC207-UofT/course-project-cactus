package com.cactus.systems;

import com.cactus.adapters.*;
import com.cactus.adapters.Response;
import com.cactus.adapters.Response.Status;

import java.util.ArrayList;
import java.util.HashMap;

/***
 * Represents the system that controls grocery lists and grocery items
 */
public class GroceryListSystem {

    private GroceryAdapter groceryListManager;
    long currentGroceryListId;

    /***
     * Create a new GroceryListSystem with groceryList managers
     */
    public GroceryListSystem(GroceryAdapter groceryListManager){
        this.groceryListManager = groceryListManager;
    }

    /***
     * Given a name from UI, creates a new GroceryList.
     * It will return false when the .createList() function returns a null
     * telling us that the name was already taken
     *
     * @param name
     * @return true if a new groceryList was created, false otherwise
     */
    public boolean newGroceryList(String name, long userid){
        Response groceryListResponse = this.groceryListManager.createGroceryList(name, userid);

        if (groceryListResponse.getStatusCode() == Status.OK) {
            this.currentGroceryListId = Long.parseLong(groceryListResponse.getPayload().get("listid"));
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

}

