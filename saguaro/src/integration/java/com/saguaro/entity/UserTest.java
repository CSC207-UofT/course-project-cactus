package com.saguaro.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserTest {

    @Autowired
    TestEntityManager testEntityManager;

    @BeforeEach
    void setUpUser() {

    }

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

    @Nested
    class TestAddFriend {

        User user;
        User friend;

        @BeforeEach
        void setUpAddFriend() {
            user = new User();
            user.setUsername("user");
            user.setPassword("password");
            user.setName("name");
            user = testEntityManager.persistFlushFind(user);

            friend = new User();
            friend.setUsername("friend");
            friend.setPassword("password");
            friend.setName("name");
            friend = testEntityManager.persistFlushFind(friend);
        }

        @Test
        void testAddFriend() {
            user.addFriend(friend);
            User savedUser = testEntityManager.persistFlushFind(user);

            friend = testEntityManager.refresh(friend);
            List<User> befriended = (List<User>) ReflectionTestUtils.getField(friend, "befriended");
            assert befriended != null; // sanity check

            assertEquals(1, savedUser.getFriends().size());
            assertTrue(savedUser.getFriends().contains(friend));

            assertEquals(1, befriended.size());
            assertTrue(befriended.contains(savedUser));
        }

        @Test
        void testAddFriendSelf() {
            user.addFriend(user);
            User savedUser = testEntityManager.persistFlushFind(user);

            friend = testEntityManager.refresh(friend);
            List<User> befriended = (List<User>) ReflectionTestUtils.getField(friend, "befriended");
            assert befriended != null; // sanity check

            assertEquals(0, savedUser.getFriends().size());
            assertEquals(0, befriended.size());
        }

        @Test
        void testAddFriendExistingFriend() {
            user.addFriend(friend);
            testEntityManager.persistAndFlush(user);
            friend = testEntityManager.refresh(friend);

            user.addFriend(friend);
            User savedUser = testEntityManager.persistFlushFind(user);

            friend = testEntityManager.refresh(friend);
            List<User> befriended = (List<User>) ReflectionTestUtils.getField(friend, "befriended");
            assert befriended != null; // sanity check

            assertEquals(1, savedUser.getFriends().size());
            assertTrue(savedUser.getFriends().contains(friend));

            assertEquals(1, befriended.size());
            assertTrue(befriended.contains(savedUser));
        }
    }

    @Nested
    class RemoveFriendTest {

    }
}
