package com.saguaro.repository;

import com.saguaro.entity.GroceryList;
import com.saguaro.entity.User;
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
public class GroceryListRepositoryTest { // TODO: write tests

    @Autowired
    GroceryListRepository groceryListRepository;

    @Autowired
    TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testSaveNewList() {
    }

}
