package com.saguaro.service;

import com.saguaro.entity.GroceryList;
import com.saguaro.entity.User;
import com.saguaro.repository.GroceryListRepository;
import com.saguaro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroceryService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroceryListRepository groceryListRepository;

    public List<String> getListNamesByUsername(String username) {
        User user = userRepository.findUserByUsername(username);

        return user.getGroceryLists().stream().map(GroceryList::getName).collect(Collectors.toList());
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

}
