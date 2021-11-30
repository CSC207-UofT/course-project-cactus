package com.saguaro.service;

import com.saguaro.entity.GroceryItem;
import com.saguaro.entity.GroceryList;
import com.saguaro.entity.User;
import com.saguaro.exception.ResourceNotFoundException;
import com.saguaro.repository.GroceryItemRepository;
import com.saguaro.repository.GroceryListRepository;
import com.saguaro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GroceryService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroceryListRepository groceryListRepository;

    @Autowired
    GroceryItemRepository groceryItemRepository;

    /**
     * Given a username, gets all the grocery lists that the corresponding user owns. Returns
     * the results as a map from list ID to list name.
     * <p>
     * Delegates to {@link #getListNamesByUsername(String, boolean)}.
     *
     * @param username the String username of the user to fetch for
     * @return a mapping from list ID to list name of all the lists the user owns
     */
    public Map<Long, String> getOwnedListNamesByUsername(String username) {
        return getListNamesByUsername(username, false);
    }

    /**
     * Given a username, gets all the grocery lists that the corresponding user has shared
     * access to. Returns the results as a map from list ID to list name.
     * <p>
     * Delegates to {@link #getListNamesByUsername(String, boolean)}.
     *
     * @param username the String username of the user to fetch for
     * @return a mapping from list ID to list name of all the lists the user has shared access to
     */
    public Map<Long, String> getSharedListNamesByUsername(String username) {
        return getListNamesByUsername(username, true);
    }

    /**
     * Fetch the either the owned or shared access lists of a user, given a username and a
     * boolean specifier.
     *
     * @param username the String username of the user to fetch for
     * @param shared   a boolean that should be true if the shared access lists are desired, and false
     *                 if the owned lists are desired
     * @return a mapping from list ID to list name of the desired type of list
     */
    private Map<Long, String> getListNamesByUsername(String username, boolean shared) {
        User user = userRepository.findUserByUsername(username);

        List<GroceryList> lists = shared ? user.getSharedLists() : user.getGroceryLists();

        return lists
                .stream()
                .collect(Collectors.toMap(GroceryList::getId, GroceryList::getName));
    }

    public GroceryList getListById(long id, String username)
            throws ResourceNotFoundException {
        User user = userRepository.findUserByUsername(username);
        GroceryList list = groceryListRepository.findGroceryListById(id);

        if (list == null || !(user.equals(list.getOwner()) || list.getSharedUsers().contains(user))) {
            throw new ResourceNotFoundException(GroceryList.class, String.valueOf(id), user);
        }

        return list;
    }

    public GroceryList createNewList(String name, String username) {
        User user = userRepository.findUserByUsername(username);

        GroceryList list = new GroceryList();
        list.setName(name);
        list.setOwner(user);

        return groceryListRepository.save(list);
    }

    public GroceryList saveList(GroceryList list, String username) throws ResourceNotFoundException {
        User user = userRepository.findUserByUsername(username);
        GroceryList oldList = groceryListRepository.findGroceryListById(list.getId());

        if (oldList == null || !(user.equals(oldList.getOwner()) || oldList.getSharedUsers().contains(user))) {
            throw new ResourceNotFoundException(GroceryList.class,
                    String.valueOf(list.getId()), user);
        }

        HashSet<GroceryItem> foundItems = new HashSet<>();

        for (GroceryItem item : list.getItems()) {
            if (!oldList.getItems().contains(item)) {
                GroceryItem savedItem = groceryItemRepository.findGroceryItemByName(item.getName());
                oldList.addItem(Objects.requireNonNullElse(savedItem, item));
            }

            foundItems.add(item);
        }

        for (int i = oldList.getItems().size() - 1; i >= 0; i--) {
            if (!foundItems.contains(oldList.getItems().get(i))) {
                oldList.removeItem(oldList.getItems().get(i));
            }
        }

        return groceryListRepository.save(oldList);
    }

    public void removeList(long id, String username) throws ResourceNotFoundException {
        User user = userRepository.findUserByUsername(username);
        GroceryList list = groceryListRepository.findGroceryListById(id);

        if (list == null || !user.equals(list.getOwner())) {
            throw new ResourceNotFoundException(GroceryList.class, String.valueOf(id), user);
        }

        groceryListRepository.delete(list);
    }

    /**
     * Add a user to the shared users of a grocery list. A list can only be shared by the owner of the list,
     * and if the user it is being shared with is a friend of the owner. If any of these conditions are
     * not met, then a ResourceNotFoundException is thrown.
     * <p>
     * Furthermore, if the user to be shared with cannot be found, or if the list ID does not match any
     * existing list, a ResourceNotFoundException is thrown. This method assumes that the username of the
     * sharer provided is valid, since a user must be authenticated to call an endpoint that calls this
     * method.
     *
     * @param id            a long representing the ID of the GroceryList to share
     * @param shareUsername the String username of the user to share the list with
     * @param username      the username of the owner of the list
     * @return the newly modified GroceryList object
     * @throws ResourceNotFoundException if the list to be shared does not belong to the sharer, or if the
     *                                   sharee is not a friend of the sharer
     */
    public GroceryList shareList(long id, String shareUsername, String username) throws ResourceNotFoundException {
        User user = userRepository.findUserByUsername(username);

        GroceryList list = groceryListRepository.findGroceryListById(id);

        if (list == null || !user.equals(list.getOwner())) {
            throw new ResourceNotFoundException(GroceryList.class, String.valueOf(id), user);
        }

        User sharee = userRepository.findUserByUsername(shareUsername);

        if (sharee == null) {
            throw new ResourceNotFoundException(User.class, shareUsername);
        } else if (!user.getFriends().contains(sharee)) {
            throw new ResourceNotFoundException(User.class, shareUsername, user);
        }

        list.addSharedUser(sharee);

        return groceryListRepository.save(list);
    }

    /**
     * Remove a user from the shared users of a grocery list. This operation can only be performed by the owner
     * of a list. In addition, only users that the list is already shared with can be "unshared" from a list. If
     * the above conditions are not met, then a ResourceNotFoundException will be thrown, with an appropriate
     * error message.
     * <p>
     * If the removal was successful, the newly modified GroceryList object is returned.
     *
     * @param id            a long representing the ID of the GroceryList to unshare
     * @param shareUsername the String username of the user to unshare the list with
     * @param username      the STring username of the user performing this operation
     * @return the newly modified GroceryList object
     * @throws ResourceNotFoundException if the list to be unshared does not belong to the authenticated user, or
     *                                   if the user to be unshared was not part of the shared users of the list
     */
    public GroceryList unshareList(long id, String shareUsername, String username) throws ResourceNotFoundException {
        User user = userRepository.findUserByUsername(username);

        GroceryList list = groceryListRepository.findGroceryListById(id);

        if (list == null || !user.equals(list.getOwner())) {
            throw new ResourceNotFoundException(GroceryList.class, String.valueOf(id), user);
        }

        User sharee = userRepository.findUserByUsername(shareUsername);

        if (sharee == null) {
            throw new ResourceNotFoundException(User.class, shareUsername);
        } else if (!list.getSharedUsers().contains(sharee)) {
            throw new ResourceNotFoundException("Grocery list " + id + " is not shared with User " + shareUsername);
        }

        list.removeSharedUser(sharee);

        return groceryListRepository.save(list);
    }

}
