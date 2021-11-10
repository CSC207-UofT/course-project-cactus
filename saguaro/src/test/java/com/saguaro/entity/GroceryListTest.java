package com.saguaro.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroceryListTest {

    @Test
    void testInstantiateItems() {
        GroceryList list = new GroceryList();

        assertNotNull(list.getItems());
    }

    @Test
    void testPreRemove() {
        User user = mock(User.class);
        GroceryItem item1 = mock(GroceryItem.class);
        GroceryItem item2 = mock(GroceryItem.class);

        GroceryList list = new GroceryList();
        list.setUser(user);
        list.addItem(item1);
        list.addItem(item2);

        list.removeList();

        verify(item1, times(1)).removeList(list);
        verify(item2, times(1)).removeList(list);
        verify(user, times(1)).removeGroceryList(list);
    }

    @Nested
    class SetUserTest {

        private User user;
        private GroceryList list;

        @BeforeEach
        void setUpSetUser() {
            user = mock(User.class);
            list = new GroceryList();
        }

        @Test
        void testNoUser() {
            list.setUser(user);

            assertEquals(user, list.getUser());
        }

        @Test
        void testExistingUser() {
            ReflectionTestUtils.setField(list, "user", user);

            User newUser = mock(User.class);
            list.setUser(newUser);

            assertEquals(user, list.getUser());
        }
    }

    @Nested
    class AddItemTest {

        private GroceryList list;
        private GroceryItem item;

        @BeforeEach
        void setUpSetAddItem() {
            list = new GroceryList();
            item = mock(GroceryItem.class);
        }

        @Test
        void testAddNewItem() {
            list.addItem(item);

            assertTrue(list.getItems().contains(item));
            verify(item, times(1)).addList(list);
        }

        @Test
        void testAddExistingItem() {
            ArrayList<GroceryItem> itemList = new ArrayList<GroceryItem>(List.of(item));
            ReflectionTestUtils.setField(list, "items", itemList);

            list.addItem(item);

            assertTrue(list.getItems().contains(item));
            verify(item, times(0)).addList(list);
            assertEquals(1, list.getItems().size());
        }
    }

    @Nested
    class RemoveItemTest {

        private GroceryList list;
        private GroceryItem item;

        @BeforeEach
        void setUpSetRemoveItem() {
            list = new GroceryList();
            item = mock(GroceryItem.class);
            ArrayList<GroceryItem> itemList = new ArrayList<>(List.of(item));
            ReflectionTestUtils.setField(list, "items", itemList);
        }

        @Test
        void testRemoveExistingItem() {
            list.removeItem(item);

            assertEquals(0, list.getItems().size());
            verify(item, times(1)).removeList(list);
        }

        @Test
        void testRemoveNonExistingItem() {
            GroceryItem newItem = mock(GroceryItem.class);

            list.removeItem(newItem);

            verify(newItem, times(0)).removeList(list);
            assertEquals(1, list.getItems().size());
            assertTrue(list.getItems().contains(item));
        }
    }

    @Nested
    class EqualsTest {

        User user;

        @BeforeEach
        void setUpEquals() {
            user = mock(User.class);
        }

        @Test
        void testEqualsSameObj() {
            GroceryList first = new GroceryList();
            first.setName("Harry's List");
            first.setUser(user);
            ReflectionTestUtils.setField(first, "id", 1);

            assertEquals(first, first);
        }

        @Test
        void testEqualsTrue() {
            GroceryList first = new GroceryList();
            first.setName("Harry's List");
            first.setUser(user);
            ReflectionTestUtils.setField(first, "id", 1);

            GroceryList second = new GroceryList();
            second.setName("Harry's List");
            second.setUser(user);
            ReflectionTestUtils.setField(second, "id", 1);

            assertEquals(first, second);
        }

        @Test
        void testEqualsNullId() {
            GroceryList first = new GroceryList();
            first.setName("Harry's List");
            first.setUser(user);
            ReflectionTestUtils.setField(first, "id", 1);

            GroceryList second = new GroceryList();
            second.setName("Harry's List");
            second.setUser(user);

            assertNotEquals(first, second);
        }

        @Test
        void testEqualsNullItems() {
            GroceryList first = new GroceryList();
            first.setName("Harry's List");
            first.setUser(user);
            ReflectionTestUtils.setField(first, "id", 1);
            ReflectionTestUtils.setField(first, "items", null);

            GroceryList second = new GroceryList();
            second.setName("Harry's List");
            second.setUser(user);
            ReflectionTestUtils.setField(second, "id", 1);

            assertThrows(NullPointerException.class, () -> {
                first.equals(second);
            });

            ReflectionTestUtils.setField(first, "items", new ArrayList<>());
            ReflectionTestUtils.setField(second, "items", null);

            assertNotEquals(first, second);
        }

        @Test
        void testEqualsNull() {
            GroceryList first = new GroceryList();
            first.setName("Harry's List");
            first.setUser(user);
            ReflectionTestUtils.setField(first, "id", 1);

            assertNotEquals(first, null);
        }

        @Test
        void testEqualsDiffClass() {
            GroceryList first = new GroceryList();
            first.setName("Harry's List");
            first.setUser(user);
            ReflectionTestUtils.setField(first, "id", 1);

            assertNotEquals(first, new HashMap<Character, String>());
        }
    }

}