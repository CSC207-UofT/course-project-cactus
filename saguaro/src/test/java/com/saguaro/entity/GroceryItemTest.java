package com.saguaro.entity;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GroceryItemTest {

    @Test
    void testInstantiateLists() {
        GroceryItem item = new GroceryItem();

        assertNotNull(item.getLists());
    }

    @Test
    void testAddList() {
        GroceryItem item = new GroceryItem();
        GroceryList list = mock(GroceryList.class);

        item.addList(list);

        assertTrue(item.getLists().contains(list));
    }

    @Test
    void testRemoveList() {
        GroceryItem item = new GroceryItem();
        GroceryList list = mock(GroceryList.class);
        ReflectionTestUtils.setField(item, "lists", new HashSet<>(List.of(list)));

        item.removeList(list);

        assertEquals(0, item.getLists().size());
    }

    @Nested
    class EqualsTest {

        @Test
        void testEqualsSameObj() {
            GroceryItem first = new GroceryItem();
            first.setName("Bread");

            assertEquals(first, first);
        }

        @Test
        void testEqualsTrue() {
            GroceryItem first = new GroceryItem();
            first.setName("Bread");

            GroceryItem second = new GroceryItem();
            second.setName("Bread");

            assertEquals(first, second);
        }

        @Test
        void testEqualsNullName() {
            GroceryItem first = new GroceryItem();

            GroceryItem second = new GroceryItem();
            second.setName("Bread");

            assertThrows(NullPointerException.class, () -> {
                first.equals(second);
            });

            first.setName("Bread");
            second.setName(null);

            assertNotEquals(first, second);
        }

        @Test
        void testEqualsNull() {
            GroceryItem first = new GroceryItem();
            first.setName("Bread");

            assertNotEquals(first, null);
        }

        @Test
        void testEqualsDiffClass() {
            GroceryItem first = new GroceryItem();
            first.setName("Bread");

            assertNotEquals(first, new HashMap<Character, String>());
        }
    }
}