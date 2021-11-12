package com.saguaro.controller;

import com.saguaro.entity.GroceryList;
import com.saguaro.exception.ResourceNotFoundException;
import com.saguaro.service.GroceryService;
import com.saguaro.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class GroceryController {

    private final GroceryService groceryService;

    public GroceryController(GroceryService groceryService) {
        this.groceryService = groceryService;
    }

    @GetMapping("api/all-lists")
    public Map<Long, String> getLists() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        return groceryService.getListNamesByUsername(username);
    }

    @GetMapping("api/list")
    public GroceryList getList(@RequestParam("id") long id) throws ResourceNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        return groceryService.getListById(id, username);
    }

    @PostMapping("api/create-list")
    public GroceryList createNewList(@RequestParam("name") String name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        return groceryService.createNewList(name, username);
    }

    @PutMapping("api/save-list")
    public GroceryList saveList(@Validated @RequestBody GroceryList list) throws ResourceNotFoundException{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        return groceryService.saveList(list, username);
    }

    @DeleteMapping("api/delete-list")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteList(@RequestParam("id") long id) throws ResourceNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        groceryService.removeList(id, username);
    }
}
