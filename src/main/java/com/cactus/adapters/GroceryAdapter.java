package com.cactus.adapters;

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
     * @param userid a long representing the user ID to fetch lists for
     * @return       a Response to the request to get grocery lists for the user
     */
    public Response getGroceryListsByUser(long userid);

    /**
     * Returns a Response object with the results of a grocery list fetch operation
     * done using the provided list ID. A user ID is required for authorization.
     *
     * If the list fetch was successful, the Response will have code 200 (OK), and
     * a payload with the following entries:
     * - listid: a string representation of the list's ID (long)
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
     * @param listid a long representing the ID of the list to get
     * @param userid a long representing the ID of the user the list belongs to
     * @return       a Response to the grocery list get operation
     */
    public Response getGroceryList(long listid, long userid);

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
     * @param userid   a long representing the ID of the user creating the
     *                     list
     * @return         a Response to the grocery list creation operation
     */
    public Response createGroceryList(String nameList, long userid);

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
     * @param listid a long representing the ID of the list to change
     * @param userid a long representing the ID of the list's owner
     * @return       a Response to the set grocery item operation
     */
    public Response setGroceryItems(List<String> items, long listid, long userid);

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
     * @param listid a long representing the ID of the grocery list to delete
     * @param userid a long representing the ID of the grocery list's owner
     * @return       a Response to the grocery list deletion operation
     */
    public Response deleteGroceryList(long listid, long userid);

}
