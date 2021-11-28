package com.saguaro.entity;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testRoleInitNotNull() {
        User user = new User();
        assertNotNull(user.getAuthorities());
    }

    @Test
    void testListsInitNotNull() {
        User user = new User();
        assertNotNull(user.getGroceryLists());
    }

    @Test
    void testFriendsInitNotNull() {
        User user = new User();
        assertNotNull(user.getFriends());
    }

    @Test
    void testBefriendedInitNotNull() {
        User user = new User();
        assertNotNull(ReflectionTestUtils.getField(user, "befriended"));
    }
}
