package com.saguaro.controller;

import com.saguaro.entity.GroceryList;
import com.saguaro.entity.User;
import com.saguaro.service.GroceryService;
import com.saguaro.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class GroceryController {

    private final GroceryService groceryService;

    private final UserService userService;

    public GroceryController(GroceryService groceryService, UserService userService) {
        this.groceryService = groceryService;
        this.userService = userService;
    }

    @GetMapping("api/all-lists")
    public List<String> getLists() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        return groceryService.getListNamesByUsername(username);
    }

    @GetMapping("api/list")
    public GroceryList getList(@RequestParam("id") long id) {
        GroceryList list = groceryService.getListById(id);

        if (list == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "List not found");
        }

        return list;
    }

    @PostMapping("api/create-list")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void createNewList(@RequestParam("name") String name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        groceryService.createNewList(name, username);
    }

    @PostMapping("api/save-list")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void saveList(@RequestParam("id") long id) {
        // TODO: finish
    }
}
