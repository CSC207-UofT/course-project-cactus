//package com.cactus.systems;
//
//import com.cactus.adapters.*;
//import com.cactus.data.EntityRepository;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Disabled
//public class GroceryListSystemTest {
//    static GroceryListSystem groceryListSystem;
//    static UserSystem userSystem;
//
//    @BeforeEach
//    public void setUp() {
//        EntityRepository repository = new EntityRepository();
////        AuthAdapter authAdapter = new ClassAuthAdapter(repository);
////        GroceryAdapter groceryAdapter = new ClassGroceryAdapter(repository);
////        userSystem = new UserSystem(authAdapter);
////        groceryListSystem = new GroceryListSystem(groceryAdapter);
//        userSystem.createUser("Caleb", "calebxcaleb", "password");
//    }
//
//    @Test
//    public void testNewGroceryListDefault() {
////        assertTrue(groceryListSystem.newGroceryList("List 1", userSystem.currentUserId));
//    }
//
//    @Disabled // TODO: fix test
//    @Test
//    public void testGetGroceryListNames() {
////        groceryListSystem.newGroceryList("List 1", userSystem.currentUserId);
////        groceryListSystem.newGroceryList("List 2", userSystem.currentUserId);
////        groceryListSystem.newGroceryList("List 3", userSystem.currentUserId);
////        groceryListSystem.newGroceryList("List 4", userSystem.currentUserId);
//
//        ArrayList<String> expected = new ArrayList<>();
//        expected.add("List 1");
//        expected.add("List 2");
//        expected.add("List 3");
//        expected.add("List 4");
//
////        ArrayList<String> actual = groceryListSystem.getGroceryListNames(userSystem.currentUserId);
//
////        assertEquals(expected, actual);
//    }
//
//    @Disabled // TODO: fix test
//    @Test
//    public void testGetGroceryItemNames() {
////        groceryListSystem.newGroceryList("List 1", userSystem.currentUserId);
//
//        List<String> expected = new ArrayList<>();
//        expected.add("Item 1");
//        expected.add("Item 2");
//        expected.add("Item 3");
//        expected.add("Item 4");
////        groceryListSystem.addGroceryItems(expected, userSystem.currentUserId);
//
////        List<String> actual = groceryListSystem.getGroceryItemNames(userSystem.currentUserId);
//
////        assertEquals(expected, actual);
//    }
//}