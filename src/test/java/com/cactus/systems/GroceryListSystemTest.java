package com.cactus.systems;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GroceryListSystemTest {
    GroceryListSystem groceryListSystem;

    @Before
    public void setUp() {
        groceryListSystem = new GroceryListSystem();
    }

    @Test
    void testCreateUserDefault() {
        assertTrue(this.groceryListSystem.createUser("Caleb", "calebxcaleb", "password"));
    }

    @Test
    void testCreateUserEmpty() {
        assertTrue(this.groceryListSystem.createUser("", "", ""));
    }

    @Test
    void testCreateUserSameUsername() {
        this.groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        assertFalse(this.groceryListSystem.createUser("Sadler", "calebxcaleb", "123"));
    }

    @Test
    void testDeleteUserDefault() {
        this.groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        assertTrue(this.groceryListSystem.deleteUser("calebxcaleb", "password"));
    }

    @Test
    void testDeleteUserDNE() {
        this.groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        assertFalse(this.groceryListSystem.deleteUser("human", "123"));
    }

    @Test
    void testLoginDefault() {
        this.groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        assertTrue(this.groceryListSystem.login("calebxcaleb", "password"));
    }

    @Test
    void testLoginWrongUsername() {
        this.groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        assertFalse(this.groceryListSystem.login("Caleb", "password"));
    }

    @Test
    void testLoginWrongPassword() {
        this.groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        assertFalse(this.groceryListSystem.login("calebxcaleb", "123"));
    }

    @Test
    void testLogoutDefault() {
        this.groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        this.groceryListSystem.logout();
        // TODO figure out a test for this method
        fail();
    }

    @Test
    void testNewGroceryListDefault() {
        this.groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        assertTrue(this.groceryListSystem.newGroceryList("List 1"));
    }

    @Test
    void testNewGroceryListSameName() {
        this.groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        this.groceryListSystem.newGroceryList("List 1");
        assertFalse(this.groceryListSystem.newGroceryList("List 1"));
    }

    @Test
    void testNewItemDefault() {
        this.groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        this.groceryListSystem.newGroceryList("List 1");
        assertTrue(this.groceryListSystem.newItem("Item 1"));
    }

    @Test
    void testNewItemSameName() {
        this.groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        this.groceryListSystem.newGroceryList("List 1");
        this.groceryListSystem.newItem("Item 1");
        assertFalse(this.groceryListSystem.newItem("Item 1"));
    }

    @Test
    void testGetGroceryListNames() {
        this.groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        this.groceryListSystem.newGroceryList("List 1");
        this.groceryListSystem.newGroceryList("List 2");
        this.groceryListSystem.newGroceryList("List 3");
        this.groceryListSystem.newGroceryList("List 4");

        String[] expected = {"List 1", "List 2", "List 3", "List 4"};
        HashMap<String, Long> actualSet = this.groceryListSystem.getGroceryListNames();
        String[] actual = actualSet.keySet().toArray(new String[4]);

        assertEquals(expected, actual);
    }

    @Test
    void testGetGroceryItemNames() {
        this.groceryListSystem.createUser("Caleb", "calebxcaleb", "password");
        this.groceryListSystem.newGroceryList("List 1");
        this.groceryListSystem.newItem("Item 1");
        this.groceryListSystem.newItem("Item 2");
        this.groceryListSystem.newItem("Item 3");
        this.groceryListSystem.newItem("Item 4");

        String[] expected = {"Item 1", "Item 2", "Item 3", "Item 4"};
        String[] actual = this.groceryListSystem.getGroceryItemNames().toArray(new String[4]);

        assertEquals(expected, actual);
    }
}