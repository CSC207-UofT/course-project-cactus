package com.cactus.systems;

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

    @BeforeEach
    public void setUp() {
        groceryListSystem = new GroceryListSystem();
    }

    @Test
    public void testCreateUserDefault() {
        assertTrue(groceryListSystem.createUser("Caleb", "calebxcaleb", "password"));
    }

    @Test
    public void testCreateUserEmpty() {
        assertTrue(groceryListSystem.createUser("", "", ""));
    }

    @Test
    public void testCreateUserSameUsername() {
        this.groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        assertFalse(groceryListSystem.createUser("Sadler", "calebxcaleb", "123"));
    }

    @Test
    public void testDeleteUserDefault() {
        groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        assertTrue(groceryListSystem.deleteUser("calebxcaleb", "password"));
    }

    @Test
    public void testDeleteUserDNE() {
        groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        assertFalse(groceryListSystem.deleteUser("human", "123"));
    }

    @Test
    public void testLoginDefault() {
        groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        assertTrue(groceryListSystem.login("calebxcaleb", "password"));
    }

    @Test
    public void testLoginWrongUsername() {
        groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        assertFalse(groceryListSystem.login("Caleb", "password"));
    }

    @Test
    public void testLoginWrongPassword() {
        groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        assertFalse(groceryListSystem.login("calebxcaleb", "123"));
    }

    @Test
    public void testLogoutDefault() {
        groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        groceryListSystem.logout();
        // TODO figure out a test for this method
        fail();
    }

    @Test
    public void testNewGroceryListDefault() {
        groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        assertTrue(groceryListSystem.newGroceryList("List 1"));
    }

    @Test
    public void testNewGroceryListSameName() {
        groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        groceryListSystem.newGroceryList("List 1");
        assertFalse(groceryListSystem.newGroceryList("List 1"));
        // TODO: Do not allow same list names
    }

    @Test
    public void testNewItemDefault() {
        groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        groceryListSystem.newGroceryList("List 1");
        assertTrue(groceryListSystem.newItem("Item 1"));
        // TODO: Do not allow same item names
    }

    @Test
    public void testNewItemSameName() {
        groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        groceryListSystem.newGroceryList("List 1");
        groceryListSystem.newItem("Item 1");
        assertFalse(groceryListSystem.newItem("Item 1"));
    }

    @Test
    public void testGetGroceryListNames() {
        groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        groceryListSystem.newGroceryList("List 1");
        groceryListSystem.newGroceryList("List 2");
        groceryListSystem.newGroceryList("List 3");
        groceryListSystem.newGroceryList("List 4");

        String[] expected = {"List 1", "List 2", "List 3", "List 4"};
        HashMap<String, Long> actualSet = groceryListSystem.getGroceryListNames();
        String[] actual = actualSet.keySet().toArray(new String[4]);
        Arrays.sort(actual);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetGroceryItemNames() {
        groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        groceryListSystem.newGroceryList("List 1");
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