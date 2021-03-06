package com.cactus.adapters;

import com.cactus.entities.GroceryList;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;

import java.util.List;

public interface GroceryAdapter {

    /**
     * Returns a List of GroceryList objects that correspond to the User associated with the given token
     * <p>
     *
     * @param token a string representing the user token to fetch lists for
     * @return a List of GroceryList objects that are of the User whose token is entered
     */
    List<GroceryList> getGroceryListNamesByUser(String token) throws InvalidParamException, ServerException;

    /**
     * Returns a list of GroceryItem objects that belong to the given list
     *
     * @param listID id of the list which holds the items you are getting
     * @param token  token of the user who holds the list
     * @return a list of GroceryItems in the list
     */
    GroceryList getGroceryList(long listID, String token) throws InvalidParamException, ServerException;

    /**
     * Returns the username of the Owner of the grocery list.
     *
     * @param listID id of the list which holds the items you are getting
     * @param token  token of the user who holds the list
     * @return the username of the Owner of the Grocery List
     */
    String getGroceryListOwner(long listID, String token) throws InvalidParamException, ServerException;

    /**
     * Create a new grocery list with the given name. Optionally mark the new list as a template,
     * or provide a template ID to initialize it with. Note that the template ID to initialize with is
     * ignored if the list is marked as a template. If a list is not a template, and should not be
     * initialized with an existing template, provide a templateID of -1.
     * <p>
     * A valid authentication token must be provided.
     *
     * @param nameList   a String containing the name of the new grocery list
     * @param token      a string representing the token of the user creating the
     *                   list
     * @param template   a boolean specifying if the created list should be a template
     * @param templateId a long representing the template ID to initialize this list with
     * @return a GroceryList that corresponds to the GroceryList created
     */
    GroceryList createGroceryList(String nameList, String token, boolean template, long templateId) throws InvalidParamException, ServerException;

    /**
     * Returns whether the list of grocery items are added to the GroceryList that corresponds
     * to the listID given. A user token is required for authorization.
     *
     * @param items  a List of Strings containing the names of grocery items to set
     * @param listID a long representing the ID of the list to change
     * @param token  a string representing the token of the list's owner
     */
    void setGroceryItems(List<String> items, long listID, String token) throws InvalidParamException, ServerException;

    /**
     * Returns whether the GroceryList corresponding to the listID is deleted.
     * A user token is required for authorization.
     *
     * @param listID a long representing the ID of the grocery list to delete
     * @param token  a string representing the token of the grocery list's owner
     */
    void deleteGroceryList(long listID, String token) throws InvalidParamException, ServerException;

    /**
     * Shares the List belonging to id to user represented by username.
     * <p>
     *
     * @param id       a long representing the ID of the grocery list to share
     * @param username The username of the User the list is being shared with
     * @param token    a string representing the token of the grocery list's owner
     */
    void shareList(long id, String username, String token) throws InvalidParamException, ServerException;

    /**
     * Unshares the List belonging to id to user represented by username.
     * <p>
     *
     * @param id       a long representing the ID of the grocery list to share
     * @param username The username of the User the list is being shared with
     * @param token    a string representing the token of the grocery list's owner
     */
    void unshareList(long id, String username, String token) throws InvalidParamException, ServerException;
}
