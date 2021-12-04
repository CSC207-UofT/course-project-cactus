package com.cactus.adapters;

import com.cactus.entities.User;

import com.cactus.exceptions.InternalException;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import okhttp3.Response;

import javax.inject.Inject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import java.util.Properties;

import static com.cactus.adapters.HttpUtil.*;


/**
 * The WebAuthAdapter class implements the AuthAdapter Interface by interacting with a server
 * that stores our Users and Grocery Lists.
 */
public class WebAuthAdapter implements AuthAdapter {

    private final String STATIC_IP;

    private final OkHttpClient client;

    @Inject
    public WebAuthAdapter(OkHttpClient client) {
        String tempIp = "192.168.0.127"; // default to this address

        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream input = classloader.getResourceAsStream("network.properties");

            if (input != null) {
                Properties props = new Properties();
                props.load(input);

                tempIp = props.getProperty("staticIp");
            }
        } catch (IOException e) {
            throw new InternalException(e);
        }

        STATIC_IP = tempIp;

        this.client = client;
    }

    /**
     * Returns a User object that corresponds to the provided username and password.
     * <p>
     * The username and password is sent to a server to return the User corresponding to the username and password.
     * <p>
     * If the username and password do not correspond to an existing User,
     * then null is returned, and no login takes place.
     * <p>
     * Note: the password is not present in the User, and a null value is in place
     *
     * @param username a String containing the username of the user to be logged in
     * @param password a String containing the password to validate
     * @return a User object that corresponds to the given parameters
     * @see User
     */
    @Override
    public User login(String username, String password) throws InvalidParamException, ServerException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(STATIC_IP)
                .port(8080)
                .addPathSegment("login")
                .addQueryParameter("username", username)
                .addQueryParameter("password", password)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(new byte[0], null))
                .build();

        String responseBody = makeRequest(this.client, request, "Invalid username/password");

        try {
            return new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(responseBody, User.class);
        } catch (JsonProcessingException e) {
            // Should always be compatible
            throw new InternalException(e);
        }
    }

    /**
     * Returns a User object created with the provided username, password, and name.
     * <p>
     * <p>
     * The username, name and password is sent to a server.
     * <p>
     * First, it checks if the username corresponds to an existing user. If it does, the function returns null, and no
     * user creation takes place.
     * <p>
     * <p>
     * If the username has a different username, then a User object corresponding to the given information is created,
     * and returned.
     * <p>
     * Note: the password is not present in the User, and a null value is in place. The token has not been set yet, and
     * is also null.
     *
     * @param name     a String containing the name of the user
     * @param username a String containing the username of the new user
     * @param password a String containing the password of the new user
     * @return a User object created with the given parameters
     * @see User
     */
    @Override
    public User create(String name, String username, String password) throws InvalidParamException, ServerException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(STATIC_IP)
                .port(8080)
                .addPathSegment("register")
                .build();

        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("username", username);
        bodyMap.put("password", password);
        bodyMap.put("name", name);

        RequestBody requestBody = null;
        try {
            requestBody = RequestBody.create((new ObjectMapper()).writeValueAsString(bodyMap),
                    MediaType.get("application/json; charset=utf-8"));
        } catch (JsonProcessingException e) {
            throw new InternalException(e);
        }

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        String responseBody = makeRequest(this.client, request);

        try {
            return new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(responseBody, User.class);
        } catch (JsonProcessingException e) {
            // Should always be compatible
            throw new InternalException(e);
        }
    }

    /**
     * Returns whether the User object with the corresponding token is successfully logged out of the application.
     * <p>
     * The token of a User object is sent to a server, which logs out the User corresponding ot the token.
     * If the token corresponds to no User object, then the User is not logged out and the function returns false.
     *
     * @param token a String containing a token unique to every User, stored in the User class
     * @return a boolean signifying whether the User corresponding to the token is logged out or not
     * @see User
     */
    @Override
    public void logout(String token) throws InvalidParamException, ServerException {
        HttpUrl.Builder baseUrl = new HttpUrl.Builder().scheme("http").host(STATIC_IP).port(8080);
        HttpUrl url = baseUrl.addPathSegment("logout").build();

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create("", null))
                .addHeader("Authorization", token)
                .build();

        makeRequest(this.client, request);
    }

    @Override
    public User editUserDetails(String name, String password, String token) throws InvalidParamException, ServerException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(STATIC_IP)
                .port(8080)
                .addPathSegment("api")
                .addPathSegment("edit-user")
                .build();

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("name", name);
        if (password != null) bodyMap.put("password", password);

        RequestBody requestBody = null;
        try {
            requestBody = RequestBody.create((new ObjectMapper()).writeValueAsString(bodyMap),
                    MediaType.get("application/json; charset=utf-8"));
        } catch (JsonProcessingException e) {
            throw new InternalException(e);
        }

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .put(requestBody)
                .build();

        String responseBody = makeRequest(this.client, request);

        try {
            return new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(responseBody, User.class);
        } catch (JsonProcessingException e) {
            // Should always be compatible
            throw new InternalException(e);
        }
    }

    @Override
    public User addFriend(String username, String token) throws InvalidParamException, ServerException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(STATIC_IP)
                .port(8080)
                .addPathSegment("api")
                .addPathSegment("add-friend")
                .addQueryParameter("username", username)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .post(RequestBody.create(new byte[0], null))
                .build();

        String responseBody = makeRequest(this.client, request, "Could not add friend " + username);

        try {
            return new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(responseBody, User.class);
        } catch (JsonProcessingException e) {
            // Should always be compatible
            throw new InternalException(e);
        }
    }
}
