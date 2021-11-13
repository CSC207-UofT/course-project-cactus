package com.saguaro.service;

import com.saguaro.entity.GroceryItem;
import com.saguaro.entity.GroceryList;
import com.saguaro.entity.User;
import com.saguaro.exception.ResourceNotFoundException;
import com.saguaro.repository.GroceryItemRepository;
import com.saguaro.repository.GroceryListRepository;
import com.saguaro.repository.UserRepository;
import org.aspectj.util.Reflection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroceryServiceTest {

    @InjectMocks
    GroceryService groceryService;

    @Mock
    UserRepository userRepository;

    @Mock
    GroceryItemRepository groceryItemRepository;

    @Mock
    GroceryListRepository groceryListRepository;

    User user;
    GroceryList list = mock(GroceryList.class);

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        user = mock(User.class);
        list = mock(GroceryList.class);

        // to access all grocery list endpoints, and thus the grocery service,
        // one needs to be authenticated, so username should always return a
        // valid user
        when(userRepository.findUserByUsername(anyString())).thenReturn(user);
    }

    @AfterEach
    void cleanUp() throws Exception {
        closeable.close();
    }

    @Nested
    class GetListNamesByUsernameTest {

        @Test
        void testGetListsValidUser() {
            when(user.getGroceryLists()).thenReturn(Arrays.asList(list, list, list));
            when(list.getId()).thenReturn(1L, 2L, 3L);
            when(list.getName()).thenReturn("name");

            Map<Long, String> actual = groceryService.getListNamesByUsername("username");

            HashMap<Long, String> expected = new HashMap<>();
            expected.put(1L, "name");
            expected.put(2L, "name");
            expected.put(3L, "name");

            assertEquals(expected, actual);
        }
    }

    @Nested
    class GetListByIdTest {

        @Test
        void testGetListByIdValid() throws Exception {
            when(groceryListRepository.findGroceryListById(anyLong())).thenReturn(list);
            when(list.getUser()).thenReturn(user);

            GroceryList actual = groceryService.getListById(1L, "username");

            assertEquals(list, actual);
        }

        @Test
        void testGetListByIdInvalidId() {
            when(groceryListRepository.findGroceryListById(anyLong())).thenReturn(null);

            assertThrows(ResourceNotFoundException.class, () -> {
                groceryService.getListById(1L, "username");
            });
        }

        @Test
        void testGetListByIdUnmatchedUser() {
            when(groceryListRepository.findGroceryListById(anyLong())).thenReturn(list);
            when(list.getUser()).thenReturn(mock(User.class));

            assertThrows(ResourceNotFoundException.class, () -> {
                groceryService.getListById(1L, "username");
            });
        }
    }

    @Nested
    class CreateListTest {

        @Test
        void testCreateListSuccess() {
            when(groceryListRepository.save(any(GroceryList.class))).thenAnswer(ans -> ans.getArgument(0));

            GroceryList list = groceryService.createNewList("name", "username");

            verify(groceryListRepository, times(1)).save(any(GroceryList.class));
            assertEquals("name", list.getName());
            assertEquals(user, list.getUser());
            assertNull(list.getItems());
        }
    }

    @Nested
    class SaveListTest {

        @Test
        void testSaveListIdenticalItemsPreserved() throws Exception {
            GroceryItem bread = mock(GroceryItem.class);

            GroceryList existingList = new GroceryList();
            existingList.setUser(user);
            existingList.addItem(bread);

            GroceryList newList = new GroceryList();
            newList.setUser(user);
            newList.addItem(bread);
            ReflectionTestUtils.setField(newList, "id", 1L);

            when(groceryListRepository.save(any(GroceryList.class))).thenAnswer(ans -> ans.getArgument(0));
            when(groceryListRepository.findGroceryListById(1L)).thenReturn(existingList);

            GroceryList actual = groceryService.saveList(newList, "username");

            verify(groceryListRepository, times(1)).save(existingList);
            assertTrue(actual.getItems().contains(bread));
            assertEquals(1, actual.getItems().size());
        }

        @Test
        void testSaveListMissingItemsRemoved() throws Exception {
            GroceryItem bread = mock(GroceryItem.class);
            GroceryItem milk = mock(GroceryItem.class);

            GroceryList existingList = new GroceryList();
            existingList.setUser(user);
            existingList.addItem(bread);
            existingList.addItem(milk);

            GroceryList newList = new GroceryList();
            newList.setUser(user);
            newList.addItem(bread);
            ReflectionTestUtils.setField(newList, "id", 1L);

            when(groceryListRepository.save(any(GroceryList.class))).thenAnswer(ans -> ans.getArgument(0));
            when(groceryListRepository.findGroceryListById(1L)).thenReturn(existingList);

            GroceryList actual = groceryService.saveList(newList, "username");

            verify(groceryListRepository, times(1)).save(existingList);
            assertTrue(actual.getItems().contains(bread));
            assertEquals(1, actual.getItems().size());
        }

        @Test
        void testSaveListNewItemsExisting() throws Exception {
            GroceryItem bread = mock(GroceryItem.class);

            GroceryItem milk = mock(GroceryItem.class);
            when(milk.getName()).thenReturn("milk");

            GroceryList existingList = new GroceryList();
            existingList.setUser(user);
            existingList.addItem(bread);

            GroceryList newList = new GroceryList();
            newList.setUser(user);
            newList.addItem(bread);
            newList.addItem(milk);
            ReflectionTestUtils.setField(newList, "id", 1L);

            when(groceryListRepository.save(any(GroceryList.class))).thenAnswer(ans -> ans.getArgument(0));
            when(groceryListRepository.findGroceryListById(1L)).thenReturn(existingList);
            when(groceryItemRepository.findGroceryItemByName("milk")).thenReturn(milk);

            GroceryList actual = groceryService.saveList(newList, "username");

            verify(groceryListRepository, times(1)).save(existingList);
            assertTrue(actual.getItems().contains(bread));
            assertTrue(actual.getItems().contains(milk));
            assertEquals(2, actual.getItems().size());
        }

        @Test
        void testSaveListNewItemsNonexisting() throws Exception {
            GroceryItem bread = mock(GroceryItem.class);

            GroceryItem milk = mock(GroceryItem.class);
            when(milk.getName()).thenReturn("milk");

            GroceryList existingList = new GroceryList();
            existingList.setUser(user);
            existingList.addItem(bread);

            GroceryList newList = new GroceryList();
            newList.setUser(user);
            newList.addItem(bread);
            newList.addItem(milk);
            ReflectionTestUtils.setField(newList, "id", 1L);

            when(groceryListRepository.save(any(GroceryList.class))).thenAnswer(ans -> ans.getArgument(0));
            when(groceryListRepository.findGroceryListById(1L)).thenReturn(existingList);
            when(groceryItemRepository.findGroceryItemByName("milk")).thenReturn(null);

            GroceryList actual = groceryService.saveList(newList, "username");

            verify(groceryListRepository, times(1)).save(existingList);
            assertTrue(actual.getItems().contains(bread));
            assertTrue(actual.getItems().contains(milk));
            assertEquals(2, actual.getItems().size());
        }

        @Test
        void testSaveListInvalidId() {
            when(groceryListRepository.findGroceryListById(anyLong())).thenReturn(null);

            assertThrows(ResourceNotFoundException.class, () -> {
                groceryService.saveList(list, "username");
            });
        }

        @Test
        void testSaveListUnmatchedUser() {
            when(groceryListRepository.findGroceryListById(anyLong())).thenReturn(list);
            when(list.getUser()).thenReturn(mock(User.class));

            assertThrows(ResourceNotFoundException.class, () -> {
                groceryService.saveList(list, "username");
            });
        }
    }

    @Nested
    class DeleteListTest {

        @Test
        void testDeleteListValid() throws Exception {
            when(groceryListRepository.findGroceryListById(anyLong())).thenReturn(list);
            when(list.getUser()).thenReturn(user);

            groceryService.removeList(1L, "username");

            verify(groceryListRepository, times(1)).delete(list);
        }

        @Test
        void testDeleteListInvalidId() {
            when(groceryListRepository.findGroceryListById(anyLong())).thenReturn(null);

            assertThrows(ResourceNotFoundException.class, () -> {
                groceryService.removeList(1L, "username");
            });
        }

        @Test
        void testDeleteListUnmatchedUser() {
            when(groceryListRepository.findGroceryListById(anyLong())).thenReturn(list);
            when(list.getUser()).thenReturn(mock(User.class));

            assertThrows(ResourceNotFoundException.class, () -> {
                groceryService.removeList(1L, "username");
            });
        }
    }
}