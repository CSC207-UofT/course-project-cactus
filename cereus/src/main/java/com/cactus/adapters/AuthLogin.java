package com.cactus.adapters;

import com.cactus.entities.User;

import org.apache.http.client.utils.URIBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class AuthLogin implements  AuthAdapter{
    @Override
    public User login(String username, String password) throws IOException, InterruptedException, URISyntaxException {
        HashMap<String, String> login = new HashMap<>();
        login.put("username", username);
        login.put("password", password);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(login);
        System.out.println(requestBody);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = new URIBuilder("http://localhost:8080/login").addParameter("username",
                username).addParameter("password", password).build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper finalMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        System.out.println(response.statusCode());
        return finalMapper.readValue(response.body(), User.class);

    }

    @Override
    public User create(String username, String password, String name) throws IOException, URISyntaxException, InterruptedException {
        HashMap<String, String> login = new HashMap<>();
        login.put("username", username);
        login.put("password", password);
        login.put("name", name);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(login);
        System.out.println(requestBody);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        ObjectMapper finalMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        System.out.println(finalMapper.readValue(response.body(), User.class));
        return login(username, password);

    }

}
