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
     *
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
     *
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
     * @param shared a boolean that should be true if the shared access lists are desired, and false
     *               if the owned lists are desired
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

        if (list == null || !user.equals(list.getOwner())) {
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

        if (oldList == null || !user.equals(oldList.getOwner())) {
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

    public void shareList(long id, String friendUsername, String username) {

    }

}
