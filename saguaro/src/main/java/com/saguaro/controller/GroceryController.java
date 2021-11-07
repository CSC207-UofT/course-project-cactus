package com.saguaro.controller;

import com.saguaro.entity.GroceryList;
import com.saguaro.service.GroceryService;
import com.saguaro.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
public class GroceryController {

    private final GroceryService groceryService;

    public GroceryController(GroceryService groceryService, UserService userService) {
        this.groceryService = groceryService;
    }

    @GetMapping("api/all-lists")
    public Map<Long, String> getLists() {
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
    public GroceryList createNewList(@RequestParam("name") String name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        return groceryService.createNewList(name, username);
    }

    @PutMapping("api/save-list")
    public GroceryList saveList(@RequestBody GroceryList list) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        return groceryService.saveList(list, username);
    }
}
