package com.saguaro.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class GroceryListTest {

    @Nested
    class TestEquals {

        User user;

        @BeforeEach
        void setUpEquals() {
            user = new User();
            user.setName("Harry Potter");
            user.setUsername("thechosenone");
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
    }

}