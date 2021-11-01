package com.saguaro.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
        list.setUser(user);

        GroceryList savedList = entityManager.persistFlushFind(list);
        user = entityManager.refresh(user);

        assertEquals(user, savedList.getUser());
        assertEquals(1, user.getGroceryLists().size());
        assertEquals(list, user.getGroceryLists().get(0));
    }

    @Test
    void testDeleteListAndRemovedListFromUser() {
        // set up, relies on adding user to list working
        GroceryList list = new GroceryList();
        list.setUser(user);
        GroceryList userList = entityManager.persistFlushFind(list);

        entityManager.remove(userList); // should call pre-remove and remove list from user
        entityManager.flush(); // write changes
        user = entityManager.refresh(user); // refresh to see if user was correctly updated

        assertEquals(0, user.getGroceryLists().size());
    }
}
