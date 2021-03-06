package com.saguaro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saguaro.entity.User;
import com.saguaro.exception.InvalidLoginException;
import com.saguaro.exception.InvalidParamException;
import com.saguaro.exception.ResourceNotFoundException;
import com.saguaro.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mvc;
    private JacksonTester<User> jsonUser;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new SaguaroExceptionHandler())
                .build();
    }

    @Nested
    class TestLogin {

        @Test
        void testValidLogin() throws Exception {
            String username = "username";
            String password = "password";

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);

            when(userService.login(username, password)).thenReturn(user);

            mvc.perform(post("/login")
                            .queryParam("username", "username")
                            .queryParam("password", "password")
                    ).andExpect(status().isOk())
                    .andExpect(result -> assertEquals(result.getResponse().getContentAsString(),
                            jsonUser.write(user).getJson()));
        }

        @Test
        void testInvalidLogin() throws Exception {
            when(userService.login(anyString(), anyString())).thenThrow(InvalidLoginException.class);

            mvc.perform(post("/login")
                            .queryParam("username", "username")
                            .queryParam("password", "password")
                    ).andExpect(result -> assertTrue(result.getResolvedException()
                            instanceof InvalidLoginException))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class TestRegister {

        @Test
        void testValidRegister() throws Exception {
            String username = "username";
            String password = "password";
            String name = "name";

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setName(name);

            when(userService.registerNewUser(username, password, name)).thenReturn(user);

            mvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"username\"" +
                                    ",\"password\":\"password\"" +
                                    ",\"name\":\"name\"}")
                    ).andExpect(status().isOk())
                    .andExpect(result -> assertEquals(result.getResponse().getContentAsString(),
                            jsonUser.write(user).getJson()));
        }

        @Test
        void testRegisterExistingUser() throws Exception {
            when(userService.registerNewUser(anyString(), anyString(), anyString())).thenThrow(InvalidParamException.class);


            mvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"username\"" +
                                    ",\"password\":\"password\"" +
                                    ",\"name\":\"name\"}")
                    ).andExpect(result -> assertTrue(result.getResolvedException()
                            instanceof InvalidParamException))
                    .andExpect(status().isConflict());
        }

        @Test
        void testRegisterUserBadRequestNoUsername() throws Exception {
            mvc.perform(post("/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(",\"password\":\"password\"" +
                            ",\"name\":\"name\"}")
            ).andExpect(status().isBadRequest());
        }

        @Test
        void testRegisterUserBadRequestNoPassword() throws Exception {
            mvc.perform(post("/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(",\"username\":\"username\"" +
                            ",\"name\":\"name\"}")
            ).andExpect(status().isBadRequest());
        }

        @Test
        void testRegisterUserBadRequestNoName() throws Exception {
            mvc.perform(post("/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(",\"username\":\"username\"" +
                            ",\"password\":\"password\"}")
            ).andExpect(status().isBadRequest());
        }

        @Test
        void testRegisterUserBadRequestBlankUsername() throws Exception {
            mvc.perform(post("/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"username\":\"\"" +
                            ",\"password\":\"password\"" +
                            ",\"name\":\"name\"}")
            ).andExpect(status().isBadRequest());
        }

        @Test
        void testRegisterUserBadRequestBlankPassword() throws Exception {
            mvc.perform(post("/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"username\":\"username\"" +
                            ",\"password\":\"\"" +
                            ",\"name\":\"name\"}")
            ).andExpect(status().isBadRequest());
        }

        @Test
        void testRegisterUserBadRequestBlankName() throws Exception {
            mvc.perform(post("/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"username\":\"username\"" +
                            ",\"password\":\"password\"" +
                            ",\"name\":\"\"}")
            ).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class TestLogout {
        @Test
        void testLogout() throws Exception {
            Authentication authentication = mock(Authentication.class);
            SecurityContext securityContext = mock(SecurityContext.class);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn("username");
            SecurityContextHolder.setContext(securityContext);


            mvc.perform(post("/logout")
                    .header("Authentication", "token")
            ).andExpect(status().isNoContent());

            verify(userService, times(1)).logout(anyString());
        }
    }

    @Nested
    class TestEditUser {

        SecurityContext securityContext;
        Authentication authentication;

        User user;

        @BeforeEach
        void setUpEditUser() {
            authentication = mock(Authentication.class);
            securityContext = mock(SecurityContext.class);
            SecurityContextHolder.setContext(securityContext);

            user = new User();
            user.setName("name");
            user.setPassword("password");
            user.setUsername("username");
        }

        @Test
        void testEditUserValid() throws Exception {
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn("username");

            when(userService.edit("name", "password", "username"))
                    .thenReturn(user);

            mvc.perform(put("/api/edit-user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"password\":\"password\"" +
                                    ",\"name\":\"name\"}")
                            .header("Authentication", "token")
                    ).andExpect(status().isOk())
                    .andExpect(result -> assertEquals(result.getResponse().getContentAsString(),
                            jsonUser.write(user).getJson()));
        }

        @Test
        void testEditUserUsernameIgnored() throws Exception {
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn("username");

            when(userService.edit("name", "password", "username"))
                    .thenReturn(user);

            mvc.perform(put("/api/edit-user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"username\"" +
                                    ",\"password\":\"password\"" +
                                    ",\"name\":\"name\"}")
                            .header("Authentication", "token")
                    ).andExpect(status().isOk())
                    .andExpect(result -> assertEquals(result.getResponse().getContentAsString(),
                            jsonUser.write(user).getJson()));
        }

        @Test
        void testEditUserNullFields() throws Exception {
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn("username");

            when(userService.edit(null, null, "username"))
                    .thenReturn(user);

            mvc.perform(put("/api/edit-user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}")
                            .header("Authentication", "token")
                    ).andExpect(status().isOk())
                    .andExpect(result -> assertEquals(result.getResponse().getContentAsString(),
                            jsonUser.write(user).getJson()));
        }

        @Test
        void testEditUserBadRequestBlankName() throws Exception {
            mvc.perform(put("/api/edit-user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"password\":\"password\"" +
                            ",\"name\":\"\"}")
                    .header("Authentication", "token")
            ).andExpect(status().isBadRequest());
        }

        @Test
        void testEditUserBadRequestBlankPassword() throws Exception {
            mvc.perform(put("/api/edit-user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"password\":\" \"" +
                            ",\"name\":\"name\"}")
                    .header("Authentication", "token")
            ).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class TestAddFriend {

        Authentication authentication;
        SecurityContext securityContext;

        @BeforeEach
        void setUpdAddFriend() {
            authentication = mock(Authentication.class);
            securityContext = mock(SecurityContext.class);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn("username");
            SecurityContextHolder.setContext(securityContext);
        }

        @Test
        void testAddFriendValid() throws Exception {
            User user = new User();
            User friend = new User();
            user.addFriend(friend);

            when(userService.addFriend("friend", "username")).thenReturn(user);

            mvc.perform(post("/api/add-friend")
                            .queryParam("username", "friend")
                    ).andExpect(status().isOk())
                    .andExpect(result -> assertEquals(result.getResponse().getContentAsString(),
                            jsonUser.write(user).getJson()));
        }

        @Test
        void testAddFriendDoesNotExist() throws Exception {
            User user = new User();
            User friend = new User();
            user.addFriend(friend);

            when(userService.addFriend("friend", "username")).thenThrow(ResourceNotFoundException.class);

            mvc.perform(post("/api/add-friend")
                            .queryParam("username", "friend")
                    ).andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(result.getResolvedException()
                            instanceof ResourceNotFoundException));
        }
    }
}