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

public class AuthLogin implements  AuthAdapter{
    @Override
    public User login(String username, String password) throws IOException, InterruptedException, URISyntaxException {
        HashMap<String, String> login = new HashMap<>();
        login.put("username", username);
        login.put("password", password);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(login);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = new URIBuilder("http://localhost:8080/login").addParameter("username",
                username).addParameter("password", password).build();
        RequestResponse result = sendRequest(login, uri);
        return result.getFinalMapper().readValue(result.getResponse().body(), User.class);

    }

    @Override
    public User create(String username, String password, String name) throws IOException, URISyntaxException, InterruptedException {
        HashMap<String, String> create = new HashMap<>();
        create.put("username", username);
        create.put("password", password);
        create.put("name", name);
        URI uri = new URI("http://localhost:8080/register");
        sendRequest(create, uri);
        return login(username, password);

    }

    private RequestResponse sendRequest(HashMap<String, String> body, URI uri) throws IOException, InterruptedException {
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
        return new RequestResponse(finalMapper, response);
    }

    private class RequestResponse {
        private final ObjectMapper finalMapper;
        private final HttpResponse<String> response;

        public RequestResponse(ObjectMapper finalMapper, HttpResponse<String> response) {
            this.finalMapper = finalMapper;
            this.response = response;
        }

        public ObjectMapper getFinalMapper() {
            return finalMapper;
        }

        public HttpResponse<String> getResponse() {
            return response;
        }
    }
    @Override
    public boolean logout(String token) throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/logout"))
                .setHeader("Authorization", token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 204;
    }


}
