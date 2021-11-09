package com.saguaro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saguaro.entity.User;
import com.saguaro.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

            MockHttpServletResponse response = mvc.perform(
                    post("/login")
                            .queryParam("username", "username")
                            .queryParam("password", "password")
            ).andReturn().getResponse();

            assertEquals(response.getStatus(), HttpStatus.OK.value());
            assertEquals(response.getContentAsString(),
                    jsonUser.write(user).getJson());
        }

        @Test
        void testInvalidLogin() throws Exception {
            when(userService.login(anyString(), anyString())).thenReturn(null);

            MockHttpServletResponse response = mvc.perform(
                    post("/login")
                            .queryParam("username", "username")
                            .queryParam("password", "password")
            ).andReturn().getResponse();

            assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());
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

            MockHttpServletResponse response = mvc.perform(
                    post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"username\"" +
                                    ",\"password\":\"password\"" +
                                    ",\"name\":\"name\"}")
            ).andReturn().getResponse();

            assertEquals(response.getStatus(), HttpStatus.OK.value());
            assertEquals(response.getContentAsString(),
                    jsonUser.write(user).getJson());
        }

        @Test
        void testRegisterExistingUser() throws Exception {
            when(userService.registerNewUser(anyString(), anyString(), anyString())).thenReturn(null);

            MockHttpServletResponse response = mvc.perform(
                    post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"username\"" +
                                    ",\"password\":\"password\"" +
                                    ",\"name\":\"name\"}")
            ).andReturn().getResponse();

            assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class TestLogout {
        @Test
        void logout() throws Exception {
            Authentication authentication = mock(Authentication.class);
            SecurityContext securityContext = mock(SecurityContext.class);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn("username");
            SecurityContextHolder.setContext(securityContext);


            MockHttpServletResponse response = mvc.perform(
                    post("/logout")
                            .header("Authentication", "token")
            ).andReturn().getResponse();

            assertEquals(response.getStatus(), HttpStatus.NO_CONTENT.value());
            verify(userService, times(1)).logout("username");
        }
    }
}