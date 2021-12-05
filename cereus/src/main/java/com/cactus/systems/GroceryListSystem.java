package com.cactus.systems;

import com.cactus.adapters.GroceryAdapter;
import com.cactus.entities.GroceryList;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * Create a new grocery list with the given name. Optionally mark the new list as a template,
     * or provide an existing template name to initialize it with. Note that the template name to initialize with is
     * ignored if the list is marked as a template. If a list is not a template, and should not be
     * initialized with an existing template, provide a templateName of null.
     *
     * A valid authentication token must be provided.
     *
     * @param name given name
     * @param token token of user
     * @param template a boolean specifying if the created list should be a template
     * @param templateName the String name of the template to initialize this list with, if it is not a template
     * @return true if a new groceryList was created, false otherwise
     */
    public void newGroceryList(String name, String token, boolean template, String templateName) throws InvalidParamException, ServerException {
        if (currentListNamesMap.containsKey(name)) {
            throw new InvalidParamException("List name already taken",
                    "List name already taken. Input was: " + name);
        }

        GroceryList newGroceryList;

        if (!template && templateName != null) {
            if (currentTemplateNamesMap.containsKey(templateName)) {
                newGroceryList = this.groceryAdapter.createGroceryList(name, token, false,
                        this.currentTemplateNamesMap.get(templateName).getId());


            } else {
                throw new InvalidParamException("Template name already taken",
                        "Template name already taken. Input was: " + name);
            }
        } else {
            newGroceryList = this.groceryAdapter.createGroceryList(name, token, template, -1);
        }

        this.currentGroceryListName = name;

        if (template) {
            this.currentTemplateNamesMap.put(name, newGroceryList);
        } else {
            this.currentListNamesMap.put(name, newGroceryList);
        }
    }

    /***
     * Return a list of all grocery list names. Can specify whether to return the set of
     * grocery template names or grocery list names.
     *
     * @param token token of user
     * @param template a boolean specifying if the created list should be a template
     * @return groceryListNameMap
     */
    public List<String> getGroceryListNames(String token, boolean template) throws InvalidParamException, ServerException {
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
    public List<String> getGroceryItemNames(String token) throws InvalidParamException, ServerException {
        return this.groceryAdapter.getGroceryItems(this.getCurrentList().getId(), token);
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
    public void addGroceryItems(List<String> items, String token) throws InvalidParamException, ServerException {
        long id = this.getCurrentList().getId();
        this.groceryAdapter.setGroceryItems(items, id, token);
    }


    /***
     * Delete the current list
     *
     * @param token the id of the user that the to be deleted list belongs to
     * @return true if the list was successfully deleted and false if list DNE
     */
    public boolean deleteGroceryList(String token, String listName) throws InvalidParamException, ServerException {

        GroceryList removed = this.currentListNamesMap.remove(listName);
        if (removed == null) {
            removed = this.currentTemplateNamesMap.remove(listName);
        }

        if (removed != null) {
            this.exitGroceryList();
            groceryAdapter.deleteGroceryList(removed.getId(), token);
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

