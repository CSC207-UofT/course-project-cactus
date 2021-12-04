package com.saguaro.repository;

import com.saguaro.entity.GroceryItem;
import com.saguaro.entity.GroceryList;
import com.saguaro.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class GroceryListRepositoryTest {

    @Autowired
    GroceryListRepository groceryListRepository;

    @Autowired
    TestEntityManager entityManager;

    private GroceryList list;

    @BeforeEach
    void setUpSave() {
        User user = new User();
        user.setName("Harry Potter");
        user.setUsername("thechosenone");
        user = entityManager.persistFlushFind(user);

        list = new GroceryList();
        list.setName("New List");
        list.setOwner(user);
    }

    @Test
    void testFindById() {
        list = entityManager.persistFlushFind(list);

        GroceryList foundList = groceryListRepository.findGroceryListById(list.getId());

        assertEquals(list, foundList);
    }

    @Test
    void testDelete() {
        GroceryItem newItem = new GroceryItem();
        newItem.setName("Bread");
        GroceryItem savedItem = entityManager.persistFlushFind(newItem);

        list.addItem(savedItem);
        list = entityManager.persistFlushFind(list);
        long id = list.getId();

        groceryListRepository.delete(list);
        entityManager.flush();

        savedItem = entityManager.refresh(savedItem);

        assertNull(entityManager.find(GroceryList.class, id));
        assertEquals(0, savedItem.getLists().size());
    }

    @Nested
    class SaveTest {

        @Test
        void testSaveListExistingItems() {
            GroceryItem newItem = new GroceryItem();
            newItem.setName("Bread");
            GroceryItem savedItem = entityManager.persistFlushFind(newItem);

            list.addItem(savedItem);

            GroceryList savedList = groceryListRepository.save(list);

            assertNotNull(entityManager.find(GroceryItem.class, "Bread"));
            assertEquals(list, entityManager.find(GroceryList.class, savedList.getId()));
        }

        @Test
        void testSaveListNewItems() {
            GroceryItem newItem = new GroceryItem();
            newItem.setName("Bread");

            list.addItem(newItem);

            GroceryList savedList = groceryListRepository.save(list);

            assertNotNull(entityManager.find(GroceryItem.class, "Bread"));
            assertEquals(list, entityManager.find(GroceryList.class, savedList.getId()));
        }
    }
}
