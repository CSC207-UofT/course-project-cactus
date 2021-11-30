package com.saguaro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saguaro.entity.GroceryItem;
import com.saguaro.entity.GroceryList;
import com.saguaro.entity.User;
import com.saguaro.exception.ResourceNotFoundException;
import com.saguaro.service.GroceryService;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GroceryControllerTest {

    @InjectMocks
    private GroceryController groceryController;

    @Mock
    private GroceryService groceryService;

    private MockMvc mvc;
    private JacksonTester<GroceryList> jsonGroceryList;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(groceryController)
                .setControllerAdvice(new SaguaroExceptionHandler())
                .build();

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getPrincipal()).thenReturn("username");
        SecurityContextHolder.setContext(securityContext);
    }

    @Nested
    class GetAllListsTest {

        @Test
        void testGetAllListsSuccess() throws Exception {
            HashMap<Long, String> lists = new HashMap<>();
            lists.put(1L, "name");

            when(groceryService.getOwnedListNames(anyString())).thenReturn(lists);

            mvc.perform(get("/api/all-lists"))
                    .andExpect(status().isOk())
                    .andExpect(result -> assertEquals((new ObjectMapper()).writeValueAsString(lists), result.getResponse().getContentAsString()));
        }
    }

    @Nested
    class GetListTest {

        @Test
        void testGetListSuccess() throws Exception {
            GroceryList list = new GroceryList();
            ReflectionTestUtils.setField(list, "id", 1L);
            list.setName("name");
            list.addItem(new GroceryItem("Bread"));

            when(groceryService.getListById(anyLong(), anyString())).thenReturn(list);

            mvc.perform(get("/api/list")
                            .queryParam("id", "1"))
                    .andExpect(status().isOk())
                    .andExpect(result -> assertEquals(jsonGroceryList.write(list).getJson(), result.getResponse().getContentAsString()));
        }

        @Test
        void testGetListNotFound() throws Exception {
            when(groceryService.getListById(anyLong(), anyString())).thenThrow(ResourceNotFoundException.class);

            mvc.perform(get("/api/list")
                            .queryParam("id", "1"))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof
                            ResourceNotFoundException));
        }

        @Test
        void testGetListBadRequest() throws Exception {
            mvc.perform(get("/api/list")
                            .queryParam("bad", "1"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class CreateListTest {

        @Test
        void testCreateListSuccess() throws Exception {
            GroceryList newList = new GroceryList();
            newList.setName("name");
            ReflectionTestUtils.setField(newList, "id", 1L);

            // getPrinciple() is stubbed to return "username"
            when(groceryService.createNewList(newList.getName(), "username", false)).thenReturn(newList);

            mvc.perform(post("/api/create-list")
                            .queryParam("name", "name"))
                    .andExpect(status().isOk())
                    .andExpect(result -> assertEquals(jsonGroceryList.write(newList).getJson(), result.getResponse().getContentAsString()));
        }

        @Test
        void testCreateTemplateSuccess() throws Exception {
            GroceryList newList = new GroceryList();
            newList.setName("name");
            newList.setTemplate(true);
            ReflectionTestUtils.setField(newList, "id", 1L);

            // getPrinciple() is stubbed to return "username"
            when(groceryService.createNewList(newList.getName(), "username", true)).thenReturn(newList);

            mvc.perform(post("/api/create-list")
                            .queryParam("name", "name")
                            .queryParam("template", "true"))
                    .andExpect(status().isOk())
                    .andExpect(result -> assertEquals(jsonGroceryList.write(newList).getJson(), result.getResponse().getContentAsString()));
        }

        @Test
        void testCreateListBadRequest() throws Exception {
            mvc.perform(post("/api/create-list")
                            .queryParam("bad", "name"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class SaveListTest {

        @Test
        void testSaveListSuccess() throws Exception {
            GroceryList saveList = new GroceryList();
            saveList.setName("name");
            saveList.addItem(new GroceryItem("bread"));
            ReflectionTestUtils.setField(saveList, "id", 1L);

            // getPrinciple() is stubbed to return "username"
            when(groceryService.saveList(saveList, "username")).thenReturn(saveList);

            mvc.perform(put("/api/save-list")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonGroceryList.write(saveList).getJson()))
                    .andExpect(status().isOk())
                    .andExpect(result -> assertEquals(jsonGroceryList.write(saveList).getJson(), result.getResponse().getContentAsString()));
        }

        @Test
        void testSaveListNotFound() throws Exception {
            GroceryList saveList = new GroceryList();
            saveList.setName("name");
            saveList.addItem(new GroceryItem("bread"));
            ReflectionTestUtils.setField(saveList, "id", 1L);

            when(groceryService.saveList(any(GroceryList.class), anyString())).thenThrow(ResourceNotFoundException.class);

            mvc.perform(put("/api/save-list")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonGroceryList.write(saveList).getJson()))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof
                            ResourceNotFoundException));
        }

        @Test
        void testSaveListBadRequestNoId() throws Exception {
            HashMap<String, String> bad = new HashMap<>();
            bad.put("items", "[\"a\", \"b\"]");

            mvc.perform(put("/api/save-list")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content((new ObjectMapper()).writeValueAsString(bad)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testSaveListBadRequestNoItems() throws Exception {
            HashMap<String, String> bad = new HashMap<>();
            bad.put("iid", "1");

            mvc.perform(put("/api/save-list")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content((new ObjectMapper()).writeValueAsString(bad)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void testSaveListBlankItem() throws Exception {
            HashMap<String, String> bad = new HashMap<>();
            bad.put("iid", "1");
            bad.put("items", "[\" \", \"b\"]");

            mvc.perform(put("/api/save-list")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content((new ObjectMapper()).writeValueAsString(bad)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class DeleteListTest {

        @Test
        void testDeleteListSuccess() throws Exception {
            mvc.perform(delete("/api/delete-list")
                            .queryParam("id", "1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        void testDeleteListNotFound() throws Exception {
            willThrow(new ResourceNotFoundException(GroceryList.class, "id", mock(User.class)))
                    .given(groceryService).removeList(anyLong(), anyString());

            mvc.perform(delete("/api/delete-list")
                            .queryParam("id", "1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testDeleteListBadRequest() throws Exception {
            mvc.perform(delete("/api/delete-list")
                            .queryParam("BAD", "1"))
                    .andExpect(status().isBadRequest());
        }
    }

}