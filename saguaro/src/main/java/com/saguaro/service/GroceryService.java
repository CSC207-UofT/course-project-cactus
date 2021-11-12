package com.saguaro.service;

import com.saguaro.entity.GroceryItem;
import com.saguaro.entity.GroceryList;
import com.saguaro.entity.User;
import com.saguaro.repository.GroceryItemRepository;
import com.saguaro.repository.GroceryListRepository;
import com.saguaro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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

    public Map<Long, String> getListNamesByUsername(String username) {
        User user = userRepository.findUserByUsername(username);

        return user.getGroceryLists()
                .stream()
                .collect(Collectors.toMap(GroceryList::getId, GroceryList::getName));
    }

    public GroceryList getListById(long id) {
        return groceryListRepository.findGroceryListById(id);
    }

    public GroceryList createNewList(String name, String username) {
        User user = userRepository.findUserByUsername(username);

        GroceryList list = new GroceryList();
        list.setName(name);
        list.setUser(user);

        return groceryListRepository.save(list);
    }

    public GroceryList saveList(GroceryList list, String username) {
        User user = userRepository.findUserByUsername(username);
        GroceryList oldList = groceryListRepository.findGroceryListById(list.getId());

        if (user == null || oldList == null || !user.equals(oldList.getUser())) {
            return null;
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

    public void removeList(long id) {
        GroceryList list = groceryListRepository.findGroceryListById(id);
        groceryListRepository.delete(list);
    }

}