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
    void testPreRemove() {
        User user = mock(User.class);
        GroceryItem item1 = mock(GroceryItem.class);
        GroceryItem item2 = mock(GroceryItem.class);

        GroceryList list = new GroceryList();
        list.setOwner(user);
        list.addItem(item1);
        list.addItem(item2);

        list.removeList();

        verify(item1, times(1)).removeList(list);
        verify(item2, times(1)).removeList(list);
        verify(user, times(1)).removeGroceryList(list);
    }

    @Nested
    class SetOwnerTest {

        private User owner;
        private GroceryList list;

        @BeforeEach
        void setUpSetOwner() {
            owner = mock(User.class);
            list = new GroceryList();
        }

        @Test
        void testNoOwner() {
            list.setOwner(owner);

            assertEquals(owner, list.getOwner());
        }

        @Test
        void testExistingOwner() {
            ReflectionTestUtils.setField(list, "owner", owner);

            User newUser = mock(User.class);
            list.setOwner(newUser);

            assertEquals(owner, list.getOwner());
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
            ArrayList<GroceryItem> itemList = new ArrayList<>(List.of(item));
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
    class AddSharedUserTest {
        GroceryList list;

        User owner;
        User friend;

        @BeforeEach
        void setUpAddSharedUser() {
            owner = mock(User.class);
            friend = mock(User.class);

            list = new GroceryList();
            list.setOwner(owner);
        }

        @Test
        void testAddSharedUser() {
            list.addSharedUser(friend);

            assertEquals(1, list.getSharedUsers().size());
            assertTrue(list.getSharedUsers().contains(friend));

            verify(friend, times(1)).addSharedList(list);
        }

        @Test
        void testAddSharedUserIsOwner() {
            list.addSharedUser(owner);

            assertEquals(0, list.getSharedUsers().size());
            verify(owner, times(0)).addSharedList(list);
        }

        @Test
        void testAddSharedUserExists() {
            ReflectionTestUtils.setField(list, "sharedUsers", new ArrayList<>(List.of(friend)));

            list.addSharedUser(friend);

            assertEquals(1, list.getSharedUsers().size());
            assertTrue(list.getSharedUsers().contains(friend));

            verify(friend, times(0)).addSharedList(list);
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
            first.setOwner(user);
            ReflectionTestUtils.setField(first, "id", 1L);

            assertEquals(first, first);
        }

        @Test
        void testEqualsTrue() {
            GroceryList first = new GroceryList();
            first.setName("Harry's List");
            first.setOwner(user);
            ReflectionTestUtils.setField(first, "id", 1L);

            GroceryList second = new GroceryList();
            second.setName("Harry's List");
            second.setOwner(user);
            ReflectionTestUtils.setField(second, "id", 1L);

            assertEquals(first, second);
        }

        @Test
        void testEqualsNullId() {
            GroceryList first = new GroceryList();
            first.setName("Harry's List");
            first.setOwner(user);
            ReflectionTestUtils.setField(first, "id", 1L);

            GroceryList second = new GroceryList();
            second.setName("Harry's List");
            second.setOwner(user);

            assertNotEquals(first, second);
        }

        @Test
        void testEqualsNullItems() {
            GroceryList first = new GroceryList();
            first.setName("Harry's List");
            first.setOwner(user);
            ReflectionTestUtils.setField(first, "id", 1);
            ReflectionTestUtils.setField(first, "items", null);

            GroceryList second = new GroceryList();
            second.setName("Harry's List");
            second.setOwner(user);
            ReflectionTestUtils.setField(second, "id", 1);

            assertThrows(NullPointerException.class, () -> first.equals(second));

            ReflectionTestUtils.setField(first, "items", new ArrayList<>());
            ReflectionTestUtils.setField(second, "items", null);

            assertNotEquals(first, second);
        }

        @Test
        void testEqualsNull() {
            GroceryList first = new GroceryList();
            first.setName("Harry's List");
            first.setOwner(user);
            ReflectionTestUtils.setField(first, "id", 1L);

            assertNotEquals(first, null);
        }

        @Test
        void testEqualsDiffClass() {
            GroceryList first = new GroceryList();
            first.setName("Harry's List");
            first.setOwner(user);
            ReflectionTestUtils.setField(first, "id", 1L);

            assertNotEquals(first, new HashMap<Character, String>());
        }
    }

}