package com.saguaro.service;

import com.saguaro.entity.Role;
import com.saguaro.entity.User;
import com.saguaro.exception.InvalidLoginException;
import com.saguaro.exception.InvalidParamException;
import com.saguaro.exception.ResourceNotFoundException;
import com.saguaro.repository.RoleRepository;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AutoCloseable closeable;

    private String username;
    private String password;
    private String name;
    private Role role;
    private User user;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        username = "username";
        password = "password";
        name = "name";
        role = new Role("ROLE_USER");

        user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.addRole(role);
    }

    @AfterEach
    void cleanUp() throws Exception {
        closeable.close();
    }

    @Nested
    class LoginTest {

        @BeforeEach()
        void setUpLogin() {
            when(userRepository.findUserByUsername(username)).thenReturn(user);
        }

        @Test
        void testValidLoginCreatesValidToken() throws Exception {
            // mock responses
            when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

            User actual = userService.login(username, password);

            // check for non-null valid UUID token
            assertNotNull(actual.getToken());

            try {
                UUID.fromString(actual.getToken());
            } catch (Exception e) {
                fail("Token is not valid UUID");
            }
        }

        @Test
        void testInvalidUsernameLogin() {
            // mock responses
            when(userRepository.findUserByUsername(username)).thenReturn(null);

            assertThrows(InvalidLoginException.class, () -> {
                userService.login(username, password);
            });
        }

        @Test
        void testInvalidPasswordLogin() {
            // mock responses
            when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

            assertThrows(InvalidLoginException.class, () -> userService.login(username, password));
        }
    }

    @Nested
    class LogoutTest {

        @BeforeEach()
        void setUpLogout() {
            when(userRepository.findUserByUsername(username)).thenReturn(user);
        }

        /*
        We assume that the username passed to logout is valid, since one must
        be authenticated to logout.
         */

        @Test
        void testLogout() {
            userService.logout(username);

            // logout invalidates token
            assertNull(user.getToken());
        }
    }

    @Nested
    class RegisterTest {

        @Test
        void testValidRegister() throws Exception {
            when(userRepository.existsByUsername(username)).thenReturn(false);
            when(roleRepository.findRoleByName("ROLE_USER")).thenReturn(role);
            when(passwordEncoder.encode(anyString())).thenReturn(password);
            when(userRepository.save(any(User.class))).thenAnswer(ans -> ans.getArgument(0));

            User newUser = userService.registerNewUser(username, password, name);

            assertEquals(user, newUser);
        }

        @Test
        void testRegisterExistingUser() {
            when(userRepository.existsByUsername(username)).thenReturn(true);

            assertThrows(InvalidParamException.class, () -> userService.registerNewUser(username, password, name));
        }
    }

    @Nested
    class FindByTokenTest {

        private String token;

        @BeforeEach
        void setUpFindByToken() {
            token = "token";

            user.setToken(token);
        }

        @Test
        void testTokenExists() {
            when(userRepository.findUserByToken(token)).thenReturn(user);

            UserDetails actual = userService.findByToken(token);

            // token is stored into the password field of UserDetails
            assertEquals(token, actual.getPassword());
        }

        @Test
        void testTokenDoesNotExist() {
            when(userRepository.findUserByToken(token)).thenReturn(null);

            UserDetails actual = userService.findByToken(token);

            assertNull(actual);
        }
    }

    @Nested
    class EditTest {

        @BeforeEach
        void setUpEdit() {
            when(userRepository.findUserByUsername(username)).thenReturn(user);
            when(userRepository.save(any(User.class))).thenAnswer(ans -> ans.getArgument(0));
        }

        @Test
        void testEditNameAndPassword() {
            when(passwordEncoder.encode("NEW_PASS")).thenReturn("HASH");

            User actual = userService.edit("NEW_NAME", "NEW_PASS", username);

            assertEquals("NEW_NAME", actual.getName());
            assertEquals("HASH", actual.getPassword());
            assertEquals(username, actual.getUsername());
        }

        @Test
        void testEditNoChange() {
            User actual = userService.edit(null, null, username);

            assertEquals(name, actual.getName());
            assertEquals(password, actual.getPassword());
            assertEquals(username, actual.getUsername());
        }
    }

    @Nested
    class AddFriendTest {

        String friendUsername;
        User friend;

        @BeforeEach
        void setUpAddFriend() {
            friendUsername = "friend";

            friend = new User();
            friend.setUsername(friendUsername);
        }

        @Test
        void testAddFriendValid() throws Exception {
            when(userRepository.findUserByUsername(username)).thenReturn(user);
            when(userRepository.findUserByUsername(friendUsername)).thenReturn(friend);
            when(userRepository.save(any(User.class))).thenAnswer(ans -> ans.getArgument(0));

            User savedUser = userService.addFriend(friendUsername, username);

            assertTrue(savedUser.getFriends().contains(friend));
        }

        @Test
        void testAddFriendDoesNotExist() {
            when(userRepository.findUserByUsername(username)).thenReturn(user);
            when(userRepository.findUserByUsername(friendUsername)).thenReturn(null);

            assertThrows(ResourceNotFoundException.class, () -> userService.addFriend(friendUsername, username));
        }
    }
}