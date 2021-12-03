package com.cactus.systems;

import com.cactus.adapters.*;
import com.cactus.entities.GroceryItem;
import com.cactus.entities.GroceryList;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

/***
 * Represents the system that controls grocery lists and grocery items
 */
@Singleton
public class GroceryListSystem {

    private final GroceryAdapter groceryAdapter;

    private String currentGroceryListName;

    private Map<String, GroceryList> currentListNamesMap;

    private Map<String, GroceryList> currentTemplateNamesMap;

    /***
     * Create a new GroceryListSystem with groceryList managers
     */
    @Inject
    public GroceryListSystem(GroceryAdapter groceryAdapter) {
        this.groceryAdapter = groceryAdapter;
    }

    /***
     * Given a name from UI, creates a new GroceryList.
     * It will return false when .createGroceryList() returns a Response with Status that is not "OK"
     * telling us that the name was already taken
     *
     * @param name given name
     * @param token token of user
     * @param template a boolean specifying if the created list should be a template
     * @return true if a new groceryList was created, false otherwise
     */
    public boolean newGroceryList(String name, String token, boolean template) {
        if (currentListNamesMap.containsKey(name)) {
            return false;
        }

        GroceryList newGroceryList = this.groceryAdapter.createGroceryList(name, token, template);

        if (newGroceryList != null) {
            this.currentGroceryListName = name;
            this.currentListNamesMap.put(name, newGroceryList);
            return true;
        }

        return false;
    }

    /***
     * Return a list of all grocery list names. Can specify whether to return the set of
     * grocery template names or grocery list names.
     *
     * @param token token of user
     * @param template a boolean specifying if the created list should be a template
     * @return groceryListNameMap
     */
    public List<String> getGroceryListNames(String token, boolean template) {
        // if one of them is null, but not the other, somethings wrong so just fetch all anew
        if (this.currentListNamesMap == null || this.currentTemplateNamesMap == null) {
            this.currentListNamesMap = new HashMap<>();
            this.currentTemplateNamesMap = new HashMap<>();
            List<GroceryList> lists = groceryAdapter.getGroceryListNamesByUser(token);

            for (GroceryList list : lists) {
                if (list.isTemplate()) {
                    this.currentTemplateNamesMap.put(list.getName(), list);
                } else {
                    this.currentListNamesMap.put(list.getName(), list);
                }
            }
        }

        Map<String, GroceryList> selectedtLists = template ? this.currentTemplateNamesMap : this.currentListNamesMap;

        return new ArrayList<>(selectedtLists.keySet());
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
                this.groceryAdapter.getGroceryItems(this.getCurrentList().getId(), token);

        for (GroceryItem groceryItem : groceryItems) {
            groceryItemNames.add(groceryItem.getName());
        }

        return groceryItemNames;
    }

    /**
     * Exit the list by setting the list id to null
     *
     * @return true if there existed a valid list id before exiting
     */
    public boolean exitGroceryList() {
        if (this.currentGroceryListName != null) {
            this.currentGroceryListName = null;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Add grocery items to the current grocery list
     *
     * @param items list of items to be added
     * @param token the token of the user that it is being added to
     * @return true of items were added successfully
     **/
    public boolean addGroceryItems(List<String> items, String token) {
        long id = this.getCurrentList().getId();
        return this.groceryAdapter.setGroceryItems(items, id, token);
    }


    /***
     * Delete the current list
     *
     * @param token the id of the user that the to be deleted list belongs to
     * @return true if the list was successfully deleted and false if list DNE
     */
    public boolean deleteGroceryList(String token, String listName) {
        // try both, if one works, the other will have no effect anyways
        GroceryList removed = this.currentListNamesMap.remove(listName);
        removed = this.currentTemplateNamesMap.remove(listName);

        if (removed != null) {
            this.exitGroceryList();
            return groceryAdapter.deleteGroceryList(removed.getId(), token);
        }

        return false;
    }

    /***
     * Return the current list name so that UI can display it.
     *
     * @return name of grocery list
     * */
    public String getListName() {
        return this.currentGroceryListName;
    }


    /***
     * Set the current grocery list to the given one
     *
     * @param listName name of the to be set grocery list
     */
    public void setCurrentGroceryList(String listName) {
        assert this.currentListNamesMap.containsKey(listName) || this.currentTemplateNamesMap.containsKey(listName);

        this.currentGroceryListName = listName;
    }

    private GroceryList getCurrentList() {
        GroceryList list = this.currentListNamesMap.get(this.currentGroceryListName);
        if (list == null) {
            list = this.currentTemplateNamesMap.get(this.currentGroceryListName);
        }

        assert list != null;

        return list;
    }

}

