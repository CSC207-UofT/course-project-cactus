package com.cactus.systems;

import com.cactus.adapters.*;
import com.cactus.data.EntityRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@Disabled // TODO: rewrite using adapters
public class GroceryListSystemTest {
    static GroceryListSystem groceryListSystem;
    static UserSystem userSystem;

    @BeforeEach
    public void setUp() {
        EntityRepository er = new EntityRepository();
        AuthAdapter authAdapter = new ClassAuthAdapter(er);
        GroceryAdapter groceryAdapter = new ClassGroceryAdapter(er);
        userSystem = new UserSystem(authAdapter);
        groceryListSystem = new GroceryListSystem(groceryAdapter);
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
        assertTrue(groceryListSystem.addGroceryItem("Item 1", userSystem.getCurrentUserId()));
        // TODO: Do not allow same item names
    }

    @Test
    public void testNewItemSameName() {
        groceryListSystem.newGroceryList("List 1", userSystem.currentUserId);
        groceryListSystem.addGroceryItem("Item 1", userSystem.getCurrentUserId());
        assertFalse(groceryListSystem.addGroceryItem("Item 1", userSystem.getCurrentUserId()));
    }

    @Test
    public void testGetGroceryListNames() {
        groceryListSystem.newGroceryList("List 1", userSystem.currentUserId);
        groceryListSystem.newGroceryList("List 2", userSystem.currentUserId);
        groceryListSystem.newGroceryList("List 3", userSystem.currentUserId);
        groceryListSystem.newGroceryList("List 4", userSystem.currentUserId);

        String[] expected = {"List 1", "List 2", "List 3", "List 4"};
        HashMap<Long, String> actualSet = groceryListSystem.getGroceryListNames(userSystem.getCurrentUserId());
        String[] actual = actualSet.keySet().toArray(new String[4]);
        Arrays.sort(actual);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetGroceryItemNames() {
        groceryListSystem.newGroceryList("List 1", userSystem.currentUserId);
        groceryListSystem.addGroceryItem("Item 1", userSystem.getCurrentUserId());
        groceryListSystem.addGroceryItem("Item 2", userSystem.getCurrentUserId());
        groceryListSystem.addGroceryItem("Item 3", userSystem.getCurrentUserId());
        groceryListSystem.addGroceryItem("Item 4", userSystem.getCurrentUserId());

        String[] expected = {"Item 1", "Item 2", "Item 3", "Item 4"};
        String[] actual = groceryListSystem.getGroceryItemNames(userSystem.getCurrentUserId()).toArray(new String[4]);
        Arrays.sort(actual);

        assertArrayEquals(expected, actual);
    }
}