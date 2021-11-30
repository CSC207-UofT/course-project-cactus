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

    public Map<Long, String> getListNamesByUsername(String username) {
        User user = userRepository.findUserByUsername(username);

        return user.getGroceryLists()
                .stream()
                .collect(Collectors.toMap(GroceryList::getId, GroceryList::getName));
    }

    public GroceryList getListById(long id, String username)
            throws ResourceNotFoundException {
        User user = userRepository.findUserByUsername(username);
        GroceryList list = groceryListRepository.findGroceryListById(id);

        if (list == null || !user.equals(list.getUser())) {
            throw new ResourceNotFoundException(GroceryList.class, String.valueOf(id), user);
        }

        return list;
    }

    @Transactional
    public GroceryList createNewList(String name, String username) {
        User user = userRepository.findUserByUsername(username);

        GroceryList list = new GroceryList();
        list.setName(name);
        list.setUser(user);

        return groceryListRepository.save(list);
    }

    @Transactional
    public GroceryList saveList(GroceryList list, String username) throws ResourceNotFoundException {
        User user = userRepository.findUserByUsername(username);
        GroceryList oldList = groceryListRepository.findGroceryListById(list.getId());

        if (oldList == null || !user.equals(oldList.getUser())) {
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

        if (list == null || list.getUser() != user) {
            throw new ResourceNotFoundException(GroceryList.class, String.valueOf(listId), user);
        }

        list.setName(newName);
        return groceryListRepository.save(list);
    }

    @Transactional
    public void removeList(long id, String username) throws ResourceNotFoundException {
        User user = userRepository.findUserByUsername(username);
        GroceryList list = groceryListRepository.findGroceryListById(id);

        if (list == null || !user.equals(list.getUser())) {
            throw new ResourceNotFoundException(GroceryList.class, String.valueOf(id), user);
        }

        groceryListRepository.delete(list);
    }

}
