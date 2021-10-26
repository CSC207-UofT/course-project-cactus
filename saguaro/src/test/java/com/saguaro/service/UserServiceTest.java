package com.saguaro.service;

import com.saguaro.entity.User;
import com.saguaro.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AutoCloseable closeable;

    private String username;
    private String password;
    private User user;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        username = "username";
        password = "password";

        user = new User();
        user.setUsername(username);
        user.setPassword(password);

        when(userRepository.findUserByUsername(username)).thenReturn(user);
    }

    @AfterEach
    void cleanUp() throws Exception {
        closeable.close();
    }

    @Nested
    class LoginTest {

        @Test
        void testValidLoginCreatesValidToken() {
            // mock responses
            when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

            User actual = userService.login(username, password);

            // check for non-null valid UUID token
            assertNotNull(actual.getToken());

            try {
                UUID.fromString(actual.getToken());
            } catch(Exception e) {
                fail("Token is not valid UUID");
            }
        }

        @Test
        void testInvalidUsernameLogin() {
            // mock responses
            when(userRepository.findUserByUsername(username)).thenReturn(null);

            User actual = userService.login(username, password);

            // invalid login returns null user
            assertNull(actual);
        }

        @Test
        void testInvalidPasswordLogin() {
            // mock responses
            when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

            User actual = userService.login(username, password);

            // invalid login returns null user
            assertNull(actual);
        }
    }

    @Nested
    class LogoutTest {

        @Test
        void testLogout() {
            userService.logout(username);

            // logout invalidates token
            assertNull(user.getToken());
        }
    }

    void registerNewUser() {
    }

    void findByToken() {
    }
}