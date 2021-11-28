package com.saguaro.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class GroceryListTest {

    @Autowired
    TestEntityManager entityManager;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Harry Potter");
        user.setUsername("thechosenone");
        user = entityManager.persistFlushFind(user);
    }

    @Test
    void testUserSetAndListAddedToUser() {
        GroceryList list = new GroceryList();
        list.setOwner(user);

        GroceryList savedList = entityManager.persistFlushFind(list);
        user = entityManager.refresh(user);

        assertEquals(user, savedList.getOwner());
        assertEquals(1, user.getGroceryLists().size());
        assertEquals(savedList, user.getGroceryLists().get(0)); // check if user contains saved list
    }

    @Nested
    class ExistingListTest {

        private GroceryList savedList;

        @BeforeEach
        void SetUpExistingList() {
            GroceryList list = new GroceryList();
            list.setOwner(user);
            savedList = entityManager.persistFlushFind(list);
        }

        @Test
        void testDeleteListAndRemovedListFromUser() {
            entityManager.remove(savedList); // should call pre-remove and remove list from user
            entityManager.flush(); // write changes
            user = entityManager.refresh(user); // refresh to see if user was correctly updated

            assertEquals(0, user.getGroceryLists().size());
        }

        @Test
        void testResetUser() {
            User newUser = new User();
            savedList.setOwner(newUser);
            entityManager.persistAndFlush(savedList);

            assertEquals(user, savedList.getOwner()); // should not be able to overwrite existing owner of list
        }

        @Nested
        class ItemsTest {

            @Test
            void testAddNewItem() {
                GroceryItem item = new GroceryItem();
                item.setName("Bread");

                /*
                Since GroceryList does not cascade any persist operation (by
                design), only already persisted grocery items can be
                added.
                 */
                assertThrows(PersistenceException.class, () -> {
                    savedList.addItem(item);
                    entityManager.persistAndFlush(savedList);
                });
            }

            @Test
            void testAddExistingItem() {
                GroceryItem item = new GroceryItem();
                item.setName("Bread");
                GroceryItem savedItem = entityManager.persistFlushFind(item);

                savedList.addItem(savedItem);
                entityManager.persistAndFlush(savedList);

                assertEquals(1, savedList.getItems().size());
                assertTrue(savedList.getItems().contains(savedItem));

                assertEquals(1, savedItem.getLists().size());
                assertTrue(savedItem.getLists().contains(savedList));
            }

            @Nested
            class ExistingItemTest {

                GroceryItem savedItem;

                @BeforeEach
                void setUpExistingItem() {
                    GroceryItem item = new GroceryItem();
                    item.setName("Bread");
                    savedItem = entityManager.persistFlushFind(item);

                    savedList.addItem(savedItem);
                    entityManager.persistAndFlush(savedList);
                }

                @Test
                void testRemovingValidItem() {
                    // test remove
                    savedList.removeItem(savedItem);
                    entityManager.persistAndFlush(savedList);

                    assertEquals(0, savedList.getItems().size());

                    assertEquals(0, savedItem.getLists().size());
                }

                @Test
                void testRemovingInvalidItem() {
                    // test remove with new item
                    GroceryItem newItem = new GroceryItem();
                    newItem.setName("Milk");

                    savedList.removeItem(newItem);
                    entityManager.persistAndFlush(savedList);

                    assertEquals(1, savedList.getItems().size());
                    assertTrue(savedList.getItems().contains(savedItem));

                    assertEquals(1, savedItem.getLists().size());
                    assertTrue(savedItem.getLists().contains(savedList));
                }

                @Test
                void testDeleteListRemovesFromItems() {
                    entityManager.remove(savedList);
                    entityManager.flush();
                    savedItem = entityManager.refresh(savedItem);

                    assertTrue(savedItem.getLists().isEmpty());
                }
            }
        }

        @Nested
        class AddSharedUserTest {

            User friend;

            @BeforeEach
            void setUpAddSharedUser() {
                friend = new User();
                friend.setName("Ron Weasley");
                friend.setUsername("ronweasley");

                friend = entityManager.persistFlushFind(friend);
            }

            @Test
            void testAddSharedUser() {
                savedList.addSharedUser(friend);
                savedList = entityManager.persistFlushFind(savedList);

                friend = entityManager.refresh(friend);

                assertEquals(1, savedList.getSharedUsers().size());
                assertTrue(savedList.getSharedUsers().contains(friend));

                assertEquals(1, friend.getSharedLists().size());
                assertTrue(friend.getSharedLists().contains(savedList));
            }

            @Test
            void testAddSharedUserIsOwner() {
                savedList.addSharedUser(user);
                savedList = entityManager.persistFlushFind(savedList);

                user = entityManager.refresh(user);

                assertTrue(savedList.getSharedUsers().isEmpty());
                assertTrue(friend.getSharedLists().isEmpty());
            }

            @Test
            void testAddSharedUserExists() {
                savedList.addSharedUser(friend);
                entityManager.persistAndFlush(savedList);

                savedList.addSharedUser(friend);
                savedList = entityManager.persistFlushFind(savedList);

                friend = entityManager.refresh(friend);

                assertEquals(1, savedList.getSharedUsers().size());
                assertTrue(savedList.getSharedUsers().contains(friend));

                assertEquals(1, friend.getSharedLists().size());
                assertTrue(friend.getSharedLists().contains(savedList));
            }
        }
    }
}
