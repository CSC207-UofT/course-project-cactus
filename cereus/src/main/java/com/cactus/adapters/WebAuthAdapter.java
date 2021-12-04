package com.cactus.adapters;

import com.cactus.entities.User;

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

import static com.cactus.adapters.HttpUtil.is4xx;
import static com.cactus.adapters.HttpUtil.is5xx;


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
            e.printStackTrace();
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
    public User login(String username, String password) {
        HashMap<String, String> login = new HashMap<>();
        login.put("username", username);
        login.put("password", password);

        HttpUrl.Builder baseUrl = new HttpUrl.Builder().scheme("http").host(STATIC_IP).port(8080);

        HttpUrl url = baseUrl.addPathSegment("login")
                .addQueryParameter("username", username)
                .addQueryParameter("password", password)
                .build();

        User user;
        try {
            user = sendRequest(login, url);
        } catch (IOException i) {
            i.printStackTrace();
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
     * @param name     a String containing the name of the user
     * @param username a String containing the username of the new user
     * @param password a String containing the password of the new user
     * @return a User object created with the given parameters
     * @see User
     */
    @Override
    public User create(String name, String username, String password) {
        HashMap<String, String> create = new HashMap<>();
        create.put("username", username);
        create.put("password", password);
        create.put("name", name);

        HttpUrl.Builder baseUrl = new HttpUrl.Builder().scheme("http").host(STATIC_IP).port(8080);
        HttpUrl url = baseUrl.addPathSegment("register").build();

        User user;
        try {
            user = sendRequest(create, url);
        } catch (IOException i) {
            i.printStackTrace();
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
        // Create body
        RequestBody requestBody = RequestBody.create((new ObjectMapper()).writeValueAsString(body),
                MediaType.get("application/json; charset=utf-8"));

        // Create request
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        // Make request
        Response response = this.client.newCall(request).execute();

        // Parse response
        ObjectMapper finalMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (response.code() != HttpUtil.HTTP_OK) {
            return null;
        }

        try {
            return finalMapper.readValue(Objects.requireNonNull(response.body()).string(), User.class);
        } catch (NullPointerException e) {
            e.printStackTrace();
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
        HttpUrl.Builder baseUrl = new HttpUrl.Builder().scheme("http").host(STATIC_IP).port(8080);
        HttpUrl url = baseUrl.addPathSegment("logout").build();

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create("", null))
                .addHeader("Authorization", token)
                .build();

        // Make request
        try {
            Response response = this.client.newCall(request).execute();

            return response.code() == HttpUtil.HTTP_NO_CONTENT;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User editUserDetails(String name, String password, String token) throws InvalidParamException, ServerException {
        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme("http")
                .host(STATIC_IP)
                .port(8080)
                .addPathSegment("api")
                .addPathSegment("edit-user");

        HttpUrl url = builder.build();

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("name", name);
        if (password != null) bodyMap.put("password", password);

        RequestBody requestBody = null;
        try {
            requestBody = RequestBody.create((new ObjectMapper()).writeValueAsString(bodyMap),
                    MediaType.get("application/json; charset=utf-8"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // should never happen, but if it does disguise as server error
            throw new ServerException("Failed to write JSON");
        }

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .put(requestBody)
                .build();

        String responseBody = this.makeRequest(request);

        try {
            return new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(responseBody, User.class);
        } catch (JsonProcessingException e) {
            // Should always be compatible
            e.printStackTrace();
            throw new ServerException(e.getMessage());
        }
    }

    private String makeRequest(Request request) throws ServerException, InvalidParamException {
        Response response;

        try {
            response = this.client.newCall(request).execute();

        } catch (IOException e) {
            throw new ServerException(e.getMessage());
        }

        if (is5xx(response.code())) {
            throw new ServerException(response.message());
        }

        if (is4xx(response.code())) {
            throw new InvalidParamException("Invalid parameters provided", response.toString());
        }

        try {
            return response.body().string();
        } catch (IOException e) {
            // this should never be invoked, so if it does i want to know what happened, print a stack trace
            e.printStackTrace();
            throw new ServerException(e.getMessage());
        }
    }
}
