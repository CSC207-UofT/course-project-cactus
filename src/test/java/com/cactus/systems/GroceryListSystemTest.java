package com.cactus.systems;

import com.cactus.adapters.*;
import com.cactus.data.EntityRepository;
import com.cactus.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
        EntityRepository repository = new EntityRepository();
        AuthAdapter authAdapter = new ClassAuthAdapter(repository);
        GroceryAdapter groceryAdapter = new ClassGroceryAdapter(repository);
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

        ArrayList<String> expected = new ArrayList<String>();
        expected.add("List 1");
        expected.add("List 2");
        expected.add("List 3");
        expected.add("List 4");

        ArrayList<String> actual = groceryListSystem.getGroceryListNames(userSystem.currentUserId);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetGroceryItemNames() {
        groceryListSystem.newGroceryList("List 1", userSystem.currentUserId);
        groceryListSystem.newItem("Item 1");
        groceryListSystem.newItem("Item 2");
        groceryListSystem.newItem("Item 3");
        groceryListSystem.newItem("Item 4");

        String[] expected = {"Item 1", "Item 2", "Item 3", "Item 4"};
        String[] actual = groceryListSystem.getGroceryItemNames(userSystem.currentUserId).toArray(new String[4]);
        Arrays.sort(actual);

        assertArrayEquals(expected, actual);
    }
}