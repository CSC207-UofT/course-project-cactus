package com.saguaro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saguaro.entity.User;
import com.saguaro.exception.InvalidLoginException;
import com.saguaro.exception.InvalidParamException;
import com.saguaro.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    }

    @Nested
    class TestLogout {
        @Test
        void logout() throws Exception {
            Authentication authentication = mock(Authentication.class);
            SecurityContext securityContext = mock(SecurityContext.class);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);


            mvc.perform(post("/logout")
                    .header("Authentication", "token")
            ).andExpect(status().isNoContent());
        }
    }
}