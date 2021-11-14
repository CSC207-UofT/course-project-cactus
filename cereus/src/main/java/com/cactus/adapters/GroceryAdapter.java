package com.cactus.adapters;

import com.cactus.entities.GroceryItem;
import com.cactus.entities.GroceryList;

import java.util.List;

public interface GroceryAdapter {

    /**
     * Returns a Response object with the results of the request to fetch grocery lists
     * for the provided user ID.
     *
     * If the fetch was successful, the Response will have code 200 (OK), and a paylod
     * with the following entries:
     * - length: a string representation of the number of grocery lists fetched (int)
     * - [x]: where x is a string representation of an integer index from 0 <= x < length.
     *        This key maps to a string representation of a grocery list ID. (long)
     *
     * The type in parentheses denotes the value's original type.
     *
     * If the request was unsuccessful (i.e. the user does not exist), then the Response
     * will have code 404 (NOT_FOUND) and a null payload.
     *
     * @param token a string representing the user token to fetch lists for
     * @return       a Response to the request to get grocery lists for the user
     */
    List<GroceryList> getGroceryListsByUser(String token);

    /**
     * Returns a Response object with the results of a grocery list fetch operation
     * done using the provided list ID. A user ID is required for authorization.
     *
     * If the list fetch was successful, the Response will have code 200 (OK), and
     * a payload with the following entries:
     * - listID: a string representation of the list's ID (long)
     * - name: a string containing the list's name (string)
     * - length: a string representation of the number of items in the list (int)
     * - [x]: where x is a string representation of an integer index from 0 <= x < length.
     *        This key maps to a string representation of a grocery item name. (string)
     *
     * The type in parentheses denotes the value's original type.
     *
     * If the list fetch was unsuccessful, the Response will have code 404 (NOT_FOUND)
     * and a null payload.
     *
     * @param listID a long representing the ID of the list to get
     * @param token a string representing the token of the user the list belongs to
     * @return       a Response to the grocery list get operation
     */
    GroceryList getGroceryList(long listID, String token);

    /**
     * get a list of grocery items that belong to the given list
     *
     * @param listID id of the list which holds the items you are getting
     * @param token token of the user who holds the list
     * @return a list of grocery items
     */
    List<GroceryItem> getGroceryItems(long listID, String token);

    /**
     * Returns a Response object with the results of a create grocery list operation
     * done using the provided list name. A user ID is required for authorization.
     *
     * If the list creation was successful, the Response will have code 200 (OK), and a
     * payload with the following entries:
     * - listid: a string representation of the list's ID (long)
     * - name: a string containing the list's name (string)
     *
     * The type in parentheses denotes the value's original type.
     *
     * If the operation was unsuccessful (i.e. the given user ID is invalid), then the
     * Response will have code 400 (BAD_REQUEST) and a null payload.
     *
     * @param nameList a String containing the name of the new grocery list
     * @param token   a string representing the token of the user creating the
     *                     list
     * @return         a Response to the grocery list creation operation
     */
    GroceryList createGroceryList(String nameList, String token);

    /**
     * Returns a Response object with the results of a set grocery item operation done
     * using the provided grocery item names and grocery list ID. A user ID is required
     * for authorization.
     *
     * If the set grocery item operation was successful, the Response will have code
     * 204 (NO_CONTENT) and a null payload.
     *
     * If the operation was unsuccessful (i.e. the given list ID does not exist for this
     * user), then the Response will have code 400 (BAD_REQUEST) and a null payload.
     *
     * @param items  a List of Strings containing the names of grocery items to set
     * @param listID a long representing the ID of the list to change
     * @param token a string representing the token of the list's owner
     * @return       a Response to the set grocery item operation
     */
    boolean setGroceryItems(List<String> items, long listID, String token);

    /**
     * Returns a Response object with the results of a delete grocery list operation done
     * using the provided grocery list ID. A user ID is required for authorization.
     *
     * If the delete grocery list operation was successful, the Response will have code
     * 204 (NO_CONTENT) and a null payload.
     *
     * If the operation was unsuccessful (i.e. the given list ID does not exist for this
     * user), then the Response will have code 400 (BAD_REQUEST) and a null payload.
     *
     * @param listID a long representing the ID of the grocery list to delete
     * @param token a string representing the token of the grocery list's owner
     * @return       a Response to the grocery list deletion operation
     */
    boolean deleteGroceryList(long listID, String token);

}
