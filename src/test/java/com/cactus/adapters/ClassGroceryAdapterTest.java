package com.cactus.adapters;

import com.cactus.data.EntityRepository;
import com.cactus.entities.GroceryItem;
import com.cactus.entities.GroceryList;
import com.cactus.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ClassGroceryAdapterTest {

    private EntityRepository er;
    private ClassGroceryAdapter cga;

    @BeforeEach
    void setUp() {
        er = new EntityRepository();
        cga = new ClassGroceryAdapter(er);
    }

    @Nested
    class NoUserTest {
        @Test
        void getGroceryListsByUserNoUser() {
            Response expected = new Response(Response.Status.NOT_FOUND);
            Response actual = cga.getGroceryListsByUser(1234L);

            assertEquals(expected, actual);
        }

        @Test
        void getGroceryListNoUser() {
            Response expected = new Response(Response.Status.NOT_FOUND);
            Response actual = cga.getGroceryList(1234L, 1234L);

            assertEquals(expected, actual);
        }

        @Test
        void createGroceryListNoUser() {
            Response expected = new Response(Response.Status.BAD_REQUEST);
            Response actual = cga.createGroceryList("My List", 1234L);

            assertEquals(expected, actual);
        }

        @Test
        void setGroceryItemsNoUser() {
            Response expected = new Response(Response.Status.BAD_REQUEST);
            Response actual = cga.setGroceryItems(new ArrayList<String>(Arrays.asList("item1", "item2")), 1234L,
                    1234L);

            assertEquals(expected, actual);
        }

        @Test
        void deleteGroceryListNoUser() {
            Response expected = new Response(Response.Status.BAD_REQUEST);
            Response actual = cga.deleteGroceryList(1234L, 1234L);

            assertEquals(expected, actual);
        }
    }

    @Nested
    class NoListTest {

        long userid;

        @BeforeEach
        void setupList() {
            User user = new User("Charles", "charles", "pass");
            er.saveUser(user);

            userid = user.getId();
        }

        @Test
        void getGroceryListNoList() {
            Response expected = new Response(Response.Status.NOT_FOUND);
            Response actual = cga.getGroceryList(1234L, userid);

            assertEquals(expected, actual);
        }

        @Test
        void setGroceryItemsNoUser() {
            Response expected = new Response(Response.Status.BAD_REQUEST);
            Response actual = cga.setGroceryItems(new ArrayList<String>(Arrays.asList("item1", "item2")),
                    1234L, userid);

            assertEquals(expected, actual);
        }

        @Test
        void deleteGroceryListNoUser() {
            Response expected = new Response(Response.Status.BAD_REQUEST);
            Response actual = cga.deleteGroceryList(1234L, userid);

            assertEquals(expected, actual);
        }
    }

    @Nested
    class ValidInputTest {

        long userid;
        long listid;

        @BeforeEach
        void setupList() {
            User user = new User("Charles", "charles", "pass");
            er.saveUser(user);

            userid = user.getId();

            GroceryList gList = new GroceryList("My List");
            gList.addUserId(userid);
            er.saveGroceryList(gList);

            listid = gList.getId();

            GroceryItem gItem = new GroceryItem("Bread", listid);
            er.saveGroceryItem(gItem);
        }

        @Test
        void getGroceryListsByUserValidInput() {
            HashMap<String, String> expectedPayload = new HashMap<>();
            expectedPayload.put("length", "1");
            expectedPayload.put("0", String.valueOf(listid));

            Response expected = new Response(Response.Status.OK, expectedPayload);
            Response actual = cga.getGroceryListsByUser(userid);

            assertEquals(expected, actual);
        }

        @Test
        void getGroceryListValidInput() {
            HashMap<String, String> expectedPayload = new HashMap<>();
            expectedPayload.put("listid", String.valueOf(listid));
            expectedPayload.put("name", "My List");
            expectedPayload.put("length", "1");
            expectedPayload.put("0", "Bread");

            Response expected = new Response(Response.Status.OK, expectedPayload);
            Response actual = cga.getGroceryList(listid, userid);

            assertEquals(expected, actual);
        }

        @Test
        void createGroceryListValidInput() {
            Response actual = cga.createGroceryList("My Second List", userid);

            long expectedId = 0L;

            for (GroceryList list : er.getGroceryListByUser(er.getUserById(userid)).toArray(new GroceryList[2])) {
                if (list.getName().equals("My Second List")) {
                    expectedId = list.getId();
                }
            }

            HashMap<String, String> expectedPayload = new HashMap<>();
            expectedPayload.put("listid", String.valueOf(expectedId));
            expectedPayload.put("name", "My Second List");

            Response expected = new Response(Response.Status.OK, expectedPayload);

            assertEquals(expected, actual);
        }

        @Test
        void setGroceryItemValidInput() {
            Response expected = new Response(Response.Status.NO_CONTENT);
            Response actual = cga.setGroceryItems(new ArrayList<String>(List.of("Milk")), listid, userid);

            User user = er.getUserById(userid);
            GroceryList list = er.getGroceryListByUser(user).toArray(new GroceryList[0])[0];

            Collection<String> expectedItems = new ArrayList<String>(List.of("Milk"));
            Collection<String> actualItems = er.getGroceryItemsByList(list).stream().map(GroceryItem::getName).collect(Collectors.toList());

            assertEquals(expected, actual);
            assertEquals(expectedItems, actualItems);
        }

        @Test
        void deleteGroceryListValidInput() {
            Response expected = new Response(Response.Status.NO_CONTENT);
            Response actual = cga.deleteGroceryList(listid, userid);

            assertEquals(expected, actual);
        }
    }
}