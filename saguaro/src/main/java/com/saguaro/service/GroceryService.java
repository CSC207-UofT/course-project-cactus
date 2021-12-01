package com.saguaro.service;

import com.saguaro.entity.GroceryItem;
import com.saguaro.entity.GroceryList;
import com.saguaro.entity.User;
import com.saguaro.exception.ResourceNotFoundException;
import com.saguaro.repository.GroceryItemRepository;
import com.saguaro.repository.GroceryListRepository;
import com.saguaro.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class GroceryService {

    UserRepository userRepository;

    GroceryListRepository groceryListRepository;

    GroceryItemRepository groceryItemRepository;

    public GroceryService(UserRepository userRepository,
                          GroceryListRepository groceryListRepository,
                          GroceryItemRepository groceryItemRepository) {
        this.userRepository = userRepository;
        this.groceryListRepository = groceryListRepository;
        this.groceryItemRepository = groceryItemRepository;
    }

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

    @Transactional
    public GroceryList createNewList(String name, String username) {
        User user = userRepository.findUserByUsername(username);

        GroceryList list = new GroceryList();
        list.setName(name);
        list.setOwner(user);

        return groceryListRepository.save(list);
    }

    @Transactional
    public GroceryList saveList(GroceryList list, String username) throws ResourceNotFoundException {
        User user = userRepository.findUserByUsername(username);
        GroceryList oldList = groceryListRepository.findGroceryListById(list.getId());

        if (oldList == null || !(user.equals(oldList.getOwner()) || oldList.getSharedUsers().contains(user))) {
            throw new ResourceNotFoundException(GroceryList.class,
                    String.valueOf(list.getId()), user);
        }

        if (list.getName() != null) {
            oldList.setName(list.getName());
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

    /**
     * Set an existing grocery list's name to some new string. This method assumes that the username
     * provided must be valid, since a user must be authenticated in order to edit a list's name.
     * <p>
     * A ResourceNotFoundException is found in the case where the provided list ID does not match
     * any grocery list belonging to the user with the provided username.
     * <p>
     * If the edit is successful, then the newly modified GroceryList is returned.
     *
     * @param listId   a long representing the ID of the grocery list to edit
     * @param newName  the String to set the name of the grocery list to
     * @param username the username of the user making the edit
     * @return the newly modified GroceryList object
     * @throws ResourceNotFoundException if the provided list ID does not match any grocery list belonging
     *                                   to the user with the provided username
     */
    @Transactional
    public GroceryList editListName(long listId, String newName, String username) throws ResourceNotFoundException {
        User user = userRepository.findUserByUsername(username);
        GroceryList list = groceryListRepository.findGroceryListById(listId);

        if (list == null || list.getOwner() != user) {
            throw new ResourceNotFoundException(GroceryList.class, String.valueOf(listId), user);
        }

        list.setName(newName);
        return groceryListRepository.save(list);
    }

    @Transactional
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
    @Transactional
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
    @Transactional
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
