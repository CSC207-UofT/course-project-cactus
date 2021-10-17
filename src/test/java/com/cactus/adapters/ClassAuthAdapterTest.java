package com.cactus.adapters;

import com.cactus.data.EntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ClassAuthAdapterTest {

    static ClassAuthAdapter classAuthAdapter;
    static EntityRepository repository;

    @BeforeEach
    public void setUp(){
        repository = new EntityRepository();
        classAuthAdapter = new ClassAuthAdapter(repository);
        classAuthAdapter.create("dorsa21", "Hello!", "dorsa");
    }

    @Test
    public void testLoginSuccess(){
        HashMap<String, String> credentials = new HashMap<String, String>();
        credentials.put("userid", "dorsa21");
        credentials.put("name", "dorsa");
        Response actual = classAuthAdapter.login("dorsa21", "Hello!");
        assertEquals(actual.getStatusCode(), Response.Status.OK);
        assertEquals(actual.getPayload(), credentials);

    }

    @Test
    public void testLoginFail(){
        Response actual1 = classAuthAdapter.login("dorsaa", "Hello!");
        Response actual2 = classAuthAdapter.login("dorsa", "Hello");
        Response actual3 = classAuthAdapter.login("dorsaa", "Hello");
        assertTrue(Objects.isNull(actual1.getPayload()));
        assertEquals(actual1.getStatusCode(), Response.Status.BAD_REQUEST);
        assertTrue(Objects.isNull(actual2.getPayload()));
        assertEquals(actual2.getStatusCode(), Response.Status.BAD_REQUEST);
        assertTrue(Objects.isNull(actual3.getPayload()));
        assertEquals(actual3.getStatusCode(), Response.Status.BAD_REQUEST);
    }

    @Test
    public void testCreateSuccess(){
        HashMap<String, String> credentials = new HashMap<String, String>();
        credentials.put("userid", "dorsa80");
        credentials.put("name", "dori");
        Response actual = classAuthAdapter.create("dorsa80", "Hi!", "dori");
        assertEquals(actual.getStatusCode(), Response.Status.OK);
        assertEquals(actual.getPayload(), credentials);

    }

    @Test
    public void testCreateSamePassword(){
        HashMap<String, String> credentials = new HashMap<String, String>();
        credentials.put("userid", "dorsa80");
        credentials.put("name", "dori");
        Response actual = classAuthAdapter.create("dorsa80", "Hello!", "dori");
        assertEquals(actual.getStatusCode(), Response.Status.OK);
        assertEquals(actual.getPayload(), credentials);

    }

    @Test
    public void testCreateSameName(){
        HashMap<String, String> credentials = new HashMap<String, String>();
        credentials.put("userid", "dorsa80");
        credentials.put("name", "dorsa");
        Response actual = classAuthAdapter.create("dorsa80", "Hi!", "dorsa");
        assertEquals(actual.getStatusCode(), Response.Status.OK);
        assertEquals(actual.getPayload(), credentials);

    }

    @Test
    public void testCreateFail(){
        Response actual = classAuthAdapter.create("dorsa21", "Hi!", "dorsa");
        assertTrue(Objects.isNull(actual.getPayload()));
        assertEquals(actual.getStatusCode(), Response.Status.BAD_REQUEST);
    }


}
