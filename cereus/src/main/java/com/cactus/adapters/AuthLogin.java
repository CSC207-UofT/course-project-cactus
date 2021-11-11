package com.cactus.adapters;

import com.cactus.entities.User;

import org.apache.http.client.utils.URIBuilder;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;


/**
 * The AuthLogin class implements the AuthAdapter Interface by interacting with a server
 * that stores our Users and Grocery Lists.
 */
public class AuthLogin implements  AuthAdapter{

    /**
     * Returns a User object that corresponds to the provided username and password.
     *
     * The username and password is sent to a server to return the User corresponding to the username and password.
     *
     * If the username and password do not correspond to an existing User,
     * then null is returned, and no login takes place.
     *
     * Note: the password is not present in the User, and a null value is in place
     *
     * @param username a String containing the username of the user to be logged in
     * @param password a String containing the password to validate
     * @return         a User object that corresponds to the given parameters
     *
     * @see User
     */
    @Override
    public User login(String username, String password) {
        HashMap<String, String> login = new HashMap<>();
        login.put("username", username);
        login.put("password", password);
        URI uri;
        try{
            uri = new URIBuilder("http://localhost:8080/login").addParameter("username",
                username).addParameter("password", password).build();
            }
        catch(URISyntaxException u) {
            return null;
        }
        User user;
        try {
            user = sendRequest(login, uri);
        }
        catch(IOException | InterruptedException i){
            return null;
        }
        if (user.getToken() == null) {
            return null;
        }
        return user;

    }

    /**
     * Returns a User object created with the provided username, password, and name.
     *
     * Note: the resulting user object is also logged in as the current user, and no further call to log in the User
     * is required
     *
     * The username, name and password is sent to a server to check if the username doesn't correspond to an existing
     * user.
     *
     * If the user has the same username and password (but different name)
     * as one that already exists, then that person is logged in and no creation takes place.
     *
     * If the user has the same username and different password as an existing user,
     * then null is returned and no creation or login takes place.
     *
     * If the username has a different username, then a User object corresponding to the given information is created,
     * and returned.
     *
     * Note: the password is not present in the User, and a null value is in place
     *
     * @param username a String containing the username of the new user
     * @param password a String containing the password of the new user
     * @param name     a String containing the name of the user
     * @return         a User object created with the given parameters
     *
     * @see User
     */
    @Override
    public User create(String username, String password, String name) {
        HashMap<String, String> create = new HashMap<>();
        create.put("username", username);
        create.put("password", password);
        create.put("name", name);
        URI uri;
        try{
            uri = new URI("http://localhost:8080/register");
        }catch(URISyntaxException u) {
            return null;
        }
        try {
            sendRequest(create, uri);
        }
        catch(IOException | InterruptedException i){
            return null;
        }
        return login(username, password);

    }

    /**
     * Sends a http request to the server and returns the User object constructed from the results.
     *
     * Takes a hashmap of the required properties and constructs them into a string.
     * The function also takes in a Universal Resource Identifier (URI), the location of where the HttpRequest is sent,
     * and the string and URI are passed into a HttpRequest.
     *
     * After the request is sent, the response is stored in a HttpResponse object. Then the body of the
     * response is mapped into a User object, which is returned
     *
     * @param body     a Hashmap containing the required properties for the HttpRequest
     * @param uri      a URI object that gives the location of the request
     * @return         a User object created from the response object
     *
     * @see User
     */
    private User sendRequest(HashMap<String, String> body, URI uri) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(body);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper finalMapper =
                new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return finalMapper.readValue(response.body(), User.class);
    }


    /**
     * Returns whether the User object with the corresponding token is successfully logged out of the application.
     *
     * The token of a User object is sent to a server, which logs out the User corresponding ot the token.
     * If the token corresponds to no User object, then the User is not logged out and the function returns false.
     *
     * @param token    a String containing a token unique to every User, stored in the User class
     * @return         a boolean signifying whether the User corresponding to the token is logged out or not
     *
     * @see User
     */
    @Override
    public boolean logout(String token) {
        HttpClient client = HttpClient.newHttpClient();
        URI uri;
        try{
            uri = new URI("http://localhost:8080/logout");
        }
        catch(URISyntaxException u){
            return false;
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .setHeader("Authorization", token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch(IOException | InterruptedException i){
            return false;
        }
        return response.statusCode() == 204;
    }


}
