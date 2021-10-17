package com.cactus.data;

import com.cactus.entities.GroceryItem;
import com.cactus.entities.GroceryList;
import com.cactus.entities.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class EntityRepository {

    private HashMap<Long, User> users;
    private HashMap<Long, GroceryList> groceryLists;
    private HashMap<Long, GroceryItem> groceryItems;

    public EntityRepository() {
        users = new HashMap<>();
        groceryLists = new HashMap<>();
        groceryItems = new HashMap<>();
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    /**
     * Retrieve a user specified by their id
     *
     * @param id the id of a user
     * @return the User with the provided id, or null otherwise
     */
    public User getUserById(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }

        return null;
    }

    public Collection<GroceryList> getAllGroceryLists() {
        return groceryLists.values();
    }

    /**
     * Retrieve a collection of grocery lists that a user has access to.
     * If the user does not exist, this method returns null.
     *
     * @param user a User in the database to get the grocery lists of
     * @return a collection of grocery lists belonging to a specified user,
     *         or null if the user does not exist
     */
    public Collection<GroceryList> getGroceryListByUser(User user) {
        if (users.containsKey(user.getId())) {

            ArrayList<GroceryList> returnList = new ArrayList<>();

            for (GroceryList gList : groceryLists.values()) {
                if (gList.getUserIdList().contains(user.getId())) {
                    returnList.add(gList);
                }
            }

            return returnList;
        }

        return null;
    }

    /**
     * Retrieve a collection of grocery items belonging to the specified
     * grocery list. If the given list does not exist, this method returns
     * null.
     *
     * @param gList a grocery list to get the items of
     * @return a collection of grocery items belonging to a specified user,
     *         or null if the user does not exist
     */
    public Collection<GroceryItem> getGroceryItemsByList(GroceryList gList) {
        if (groceryLists.containsKey(gList.getId())) {

            ArrayList<GroceryItem> returnList = new ArrayList<>();

            for (GroceryItem gItem : groceryItems.values()) {
                if (gItem.getGroceryListId() == gList.getId()) {
                    returnList.add(gItem);
                }
            }

            return returnList;
        }

        return null;
    }

    /**
     * Given a new user with an empty id of 0, generate a random id and
     * add to the database. This operation automatically fails if given
     * a user with a non-zero id.
     *
     * @param user the user to add to the database
     * @return true if the user was successfully added, false otherwise
     */
    public boolean saveUser(User user) {
        if (user.getId() == 0) {
            long generatedId = generateId(users);

            user.setId(generatedId);
        } else {
            return false;
        }

        users.put(user.getId(), user);
        return true;
    }

    public boolean saveGroceryList(GroceryList gList) {
        if (gList.getId() == 0) {
            long generatedId = generateId(groceryLists);

            gList.setId(generatedId);
        } else {
            return false;
        }

        groceryLists.put(gList.getId(), gList);
        return true;
    }

    public boolean deleteGroceryList(long listId) {
        if (groceryLists.containsKey(listId)) {
            groceryLists.remove(listId);
            return true;
        } else {
            return false;
        }
    }

    public boolean saveGroceryItem(GroceryItem gItem) {
        if (gItem.getId() == 0) {
            long generatedId = generateId(groceryLists);

            gItem.setId(generatedId);
        } else {
            return false;
        }

        groceryItems.put(gItem.getId(), gItem);
        return true;
    }

    public boolean deleteGroceryItem(long itemId) {
        if (groceryItems.containsKey(itemId)) {
            groceryItems.remove(itemId);
            return true;
        } else {
            return false;
        }
    }

    private <T> long generateId(HashMap<Long, T> map) {
        long generatedId;
        do {
            generatedId = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE) + 1;
        } while (map.containsKey(generatedId));

        return generatedId;
    }

}
