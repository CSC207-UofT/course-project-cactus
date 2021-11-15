package com.cactus.adapters;

import com.cactus.entities.GroceryItem;
import com.cactus.entities.GroceryList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import okhttp3.*;
import okhttp3.Response;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class WebGroceryAdapter implements GroceryAdapter {

    private final static int HTTP_OK = 200;
    private final static int HTTP_NO_CONTENT = 204;
    private final String STATIC_IP;

    private final OkHttpClient client;

    @Inject
    public WebGroceryAdapter() {
        String tempIp = "192.168.0.127"; // default to this address
        try {
            InputStream input = new FileInputStream("src/main/resources/network.properties");

            Properties props = new Properties();
            props.load(input);

            tempIp = props.getProperty("staticIp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        STATIC_IP = tempIp;

        client = new OkHttpClient();
    }

    @Override
    public List<GroceryList> getGroceryListsByUser(String token) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(STATIC_IP)
                .port(8080)
                .addPathSegment("api")
                .addPathSegment("all-lists").build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .build();

        HashMap<Integer, String> groceryListNames;

        try {
            // Make request
            Response response = client.newCall(request).execute();
            //Parse response
            ObjectMapper finalMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            if (response.code() != HTTP_OK) {
                return null;
            }
            groceryListNames = finalMapper.readValue(Objects.requireNonNull(response.body()).string(),
                    new TypeReference<HashMap<Integer, String>>() {
                    });

        } catch (NullPointerException | IOException i) {
            i.printStackTrace();
            return null;
        }

        // Retrieve every list whose id was returned
        ArrayList<GroceryList> groceryLists = new ArrayList<>();
        for (HashMap.Entry<Integer, String> mapElement : groceryListNames.entrySet()) {
            int id = mapElement.getKey();
            groceryLists.add(getGroceryList(id, token));
        }
        return groceryLists;

    }


    @Override
    public GroceryList getGroceryList(long listID, String token) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(STATIC_IP)
                .port(8080)
                .addPathSegment("api")
                .addPathSegment("list")
                .addQueryParameter("id", String.valueOf(listID))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .build();
        try {
            // Make request
            Response response = client.newCall(request).execute();
            //Parse response
            ObjectMapper finalMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            if (response.code() != HTTP_OK) {
                return null;
            }
            return finalMapper.readValue(Objects.requireNonNull(response.body()).string(), GroceryList.class);
        } catch (NullPointerException | IOException i) {
            i.printStackTrace();
            return null;
        }
    }

    @Override
    public List<GroceryItem> getGroceryItems(long listID, String token) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(STATIC_IP)
                .port(8080)
                .addPathSegment("api")
                .addPathSegment("list")
                .addQueryParameter("id", String.valueOf(listID))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .build();
        try {
            // Make request
            Response response = client.newCall(request).execute();
            //Parse response
            ObjectMapper finalMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            if (response.code() != HTTP_OK) {
                return null;
            }
            String responseString = Objects.requireNonNull(response.body()).string();
            JsonNode jsonNode = finalMapper.readTree(responseString);
            ArrayNode arrayNode = (ArrayNode) jsonNode.get("items");
            ArrayList<String> itemNames = new ArrayList<>();
            for (JsonNode jsonNode1 : arrayNode) {
                String itemName = jsonNode1.get("name").asText();
                itemNames.add(itemName);
            }

            // Create GroceryItem objects
            ArrayList<GroceryItem> groceryItems = new ArrayList<>();
            for (String name : itemNames) {
                GroceryItem groceryItem = new GroceryItem(name);
                groceryItems.add(groceryItem);
            }
            return groceryItems;

        } catch (NullPointerException | IOException i) {
            i.printStackTrace();
            return null;
        }

    }

    @Override
    public GroceryList createGroceryList(String nameList, String token) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(STATIC_IP)
                .port(8080)
                .addPathSegment("api")
                .addPathSegment("create-list")
                .addQueryParameter("name", nameList)
                .build();

        // Create body
        RequestBody requestBody = RequestBody.create("",
                MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .post(requestBody)
                .build();
        try {
            // Make request
            Response response = client.newCall(request).execute();

            // Parse response
            ObjectMapper finalMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            if (response.code() != HTTP_OK) {
                System.out.println(response.code());
                return null;
            }
            return finalMapper.readValue(Objects.requireNonNull(response.body()).string(), GroceryList.class);
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean setGroceryItems(List<String> items, long listID, String token) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(STATIC_IP)
                .port(8080)
                .addPathSegment("api")
                .addPathSegment("save-list")
                .build();

        HashMap<String, Object> save = new HashMap<>();
        save.put("id", listID);
        save.put("items", items);
        try {
            // Create body
            RequestBody requestBody = RequestBody.create((new ObjectMapper()).writeValueAsString(save),
                    MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", token)
                    .put(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            return response.code() == HTTP_OK;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean deleteGroceryList(long listID, String token) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(STATIC_IP)
                .port(8080)
                .addPathSegment("api")
                .addPathSegment("delete-list")
                .addQueryParameter("id", String.valueOf(listID))
                .build();
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", token)
                    .delete()
                    .build();
            Response response = client.newCall(request).execute();

            return response.code() == HTTP_NO_CONTENT;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
