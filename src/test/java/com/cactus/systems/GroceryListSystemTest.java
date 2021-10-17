package com.cactus.systems;

import com.cactus.adapters.AuthAdapter;
import com.cactus.adapters.GroceryAdapter;
import com.cactus.adapters.GroceryListManager;
import com.cactus.adapters.UserManager;
import com.cactus.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class GroceryListSystemTest {
    static GroceryListSystem groceryListSystem;
    static UserSystem userSystem;

    @BeforeEach
    public void setUp() {
        AuthAdapter userManager = new UserManager();
        GroceryAdapter groceryListManager = new GroceryListManager();
        userSystem = new UserSystem(userManager);
        groceryListSystem = new GroceryListSystem(groceryListManager);
        userSystem.createUser("Caleb", "calebxcaleb", "password");
    }

    @Test
    public void testNewGroceryListDefault() {
        assertTrue(groceryListSystem.newGroceryList("List 1", userSystem.currentUserId));
    }

    @Test
    public void testNewGroceryListSameName() {
        groceryListSystem.newGroceryList("List 1", userSystem.currentUserId);
        assertFalse(groceryListSystem.newGroceryList("List 1", userSystem.currentUserId));
        // TODO: Do not allow same list names
    }

    @Test
    public void testNewItemDefault() {
        groceryListSystem.newGroceryList("List 1", userSystem.currentUserId);
        assertTrue(groceryListSystem.newItem("Item 1"));
        // TODO: Do not allow same item names
    }

    @Test
    public void testNewItemSameName() {
        groceryListSystem.newGroceryList("List 1", userSystem.currentUserId);
        groceryListSystem.newItem("Item 1");
        assertFalse(groceryListSystem.newItem("Item 1"));
    }

    @Test
    public void testGetGroceryListNames() {
        groceryListSystem.newGroceryList("List 1", userSystem.currentUserId);
        groceryListSystem.newGroceryList("List 2", userSystem.currentUserId);
        groceryListSystem.newGroceryList("List 3", userSystem.currentUserId);
        groceryListSystem.newGroceryList("List 4", userSystem.currentUserId);

        String[] expected = {"List 1", "List 2", "List 3", "List 4"};
        HashMap<String, Long> actualSet = groceryListSystem.getGroceryListNames();
        String[] actual = actualSet.keySet().toArray(new String[4]);
        Arrays.sort(actual);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetGroceryItemNames() {
        groceryListSystem.newGroceryList("List 1", userSystem.currentUserId);
        groceryListSystem.newItem("Item 1");
        groceryListSystem.newItem("Item 2");
        groceryListSystem.newItem("Item 3");
        groceryListSystem.newItem("Item 4");

        String[] expected = {"Item 1", "Item 2", "Item 3", "Item 4"};
        String[] actual = groceryListSystem.getGroceryItemNames().toArray(new String[4]);
        Arrays.sort(actual);

        assertArrayEquals(expected, actual);
    }
}