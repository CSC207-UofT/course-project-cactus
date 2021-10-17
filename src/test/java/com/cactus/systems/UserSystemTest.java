package com.cactus.systems;

import com.cactus.adapters.AuthAdapter;
import com.cactus.adapters.ClassAuthAdapter;
import com.cactus.adapters.UserManager;
import com.cactus.data.EntityRepository;
import com.cactus.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserSystemTest {
    static UserSystem userSystem;

    @BeforeEach
    public void setUp() {
        EntityRepository repository = new EntityRepository();
        AuthAdapter authAdapter = new ClassAuthAdapter(repository);
        userSystem = new UserSystem(authAdapter);
    }

    @Test
    public void testCreateUserDefault() {
        assertTrue(userSystem.createUser("Caleb", "calebxcaleb", "password"));
    }

    @Test
    public void testCreateUserEmpty() {
        assertTrue(userSystem.createUser("", "", ""));
    }

    @Test
    public void testCreateUserSameUsername() {
        userSystem.createUser("Caleb", "calebxcaleb", "password");
        assertFalse(userSystem.createUser("Sadler", "calebxcaleb", "123"));
    }

    @Test
    public void testLoginDefault() {
        userSystem.createUser("Caleb", "calebxcaleb", "password");
        assertTrue(userSystem.login("calebxcaleb", "password"));
    }

    @Test
    public void testLoginWrongUsername() {
        userSystem.createUser("Caleb", "calebxcaleb", "password");
        assertFalse(userSystem.login("Caleb", "password"));
    }

    @Test
    public void testLoginWrongPassword() {
        userSystem.createUser("Caleb", "calebxcaleb", "password");
        assertFalse(userSystem.login("calebxcaleb", "123"));
    }

    @Test
    public void testLogoutDefault() {
        userSystem.createUser("Caleb", "calebxcaleb", "password");
        assertTrue(userSystem.logout());
    }

    @Test
    public void testLogoutUserDNE() {
        assertFalse(userSystem.logout());
    }
}