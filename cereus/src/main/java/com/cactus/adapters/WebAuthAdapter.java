package com.cactus.adapters;

import com.cactus.entities.User;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import okhttp3.Response;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;


/**
 * The WebAuthAdapter class implements the AuthAdapter Interface by interacting with a server
 * that stores our Users and Grocery Lists.
 */
public class WebAuthAdapter implements AuthAdapter {

    private final static int HTTP_OK = 200;
    private final static int HTTP_NO_CONTENT = 204;

    @Inject
    public WebAuthAdapter() {}

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
    public User login(String username, String password) {
        HashMap<String, String> login = new HashMap<>();
        login.put("username", username);
        login.put("password", password);

        HttpUrl.Builder baseUrl = new HttpUrl.Builder().scheme("http").host("192.168.0.127").port(8080);

        HttpUrl url = baseUrl.addPathSegment("login")
                .addQueryParameter("username", username)
                .addQueryParameter("password", password)
                .build();

        User user;
        try {
            user = sendRequest(login, url);
        } catch (IOException i) {
            return null;
        }
        return user;

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
     * @param username a String containing the username of the new user
     * @param password a String containing the password of the new user
     * @param name     a String containing the name of the user
     * @return a User object created with the given parameters
     * @see User
     */
    @Override
    public User create(String username, String password, String name) {
        HashMap<String, String> create = new HashMap<>();
        create.put("username", username);
        create.put("password", password);
        create.put("name", name);

        HttpUrl.Builder baseUrl = new HttpUrl.Builder().scheme("http").host("192.168.0.127").port(8080);
        HttpUrl url = baseUrl.addPathSegment("register").build();

        User user;
        try {
            user = sendRequest(create, url);
        } catch (IOException i) {
            System.out.println(i.getMessage());
            return null;
        }
        return user;

    }

    /**
     * Sends a http request to the server and returns the User object constructed from the results.
     * <p>
     * Takes a hashmap of the required properties and constructs them into a string.
     * The function also takes in a Universal Resource Identifier (URI), the location of where the HttpRequest is sent,
     * and the string and URI are passed into a HttpRequest.
     * <p>
     * After the request is sent, the response is stored in a HttpResponse object. Then the body of the
     * response is mapped into a User object, which is returned
     *
     * @param body a Hashmap containing the required properties for the HttpRequest
     * @param url  a URL object that gives the location of the request
     * @return a User object created from the response object
     * @see User
     */
    private User sendRequest(HashMap<String, String> body, HttpUrl url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Create body
        RequestBody requestBody = RequestBody.create((new ObjectMapper()).writeValueAsString(body),
                MediaType.get("application/json; charset=utf-8"));

        // Create request
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        // Make request
        Response response = client.newCall(request).execute();

        // Parse response
        ObjectMapper finalMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (response.code() != HTTP_OK) {
            return null;
        }

        try {
            return finalMapper.readValue(Objects.requireNonNull(response.body()).string(), User.class);
        }
        catch(NullPointerException e){
            return null;
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
    public boolean logout(String token) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder baseUrl = new HttpUrl.Builder().scheme("http").host("192.168.0.127").port(8080);
        HttpUrl url = baseUrl.addPathSegment("logout").build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .build();

        // Make request
        try {
            Response response = client.newCall(request).execute();

            return response.code() == HTTP_NO_CONTENT;
        } catch (IOException e) {
            return false;
        }
    }
}
