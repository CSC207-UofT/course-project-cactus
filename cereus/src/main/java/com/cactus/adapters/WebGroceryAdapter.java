package com.cactus.adapters;

import com.cactus.entities.GroceryItem;
import com.cactus.entities.GroceryList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class WebGroceryAdapter implements GroceryAdapter {

    private final static int HTTP_OK = 200;
    private final static int HTTP_NO_CONTENT = 204;

    @Inject
    public WebGroceryAdapter() {}

    @Override
    public List<GroceryList> getGroceryListsByUser(String token) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder baseUrl = new HttpUrl.Builder().scheme("http").host("192.168.0.127").port(8080);
        HttpUrl url = baseUrl.addPathSegment("api").addPathSegment("all-lists").build();

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
                    new TypeReference<HashMap<Integer, String>>(){});

        }catch (NullPointerException | IOException i) {
            return null;
        }

        // Retrieve every list whose id was returned
        ArrayList<GroceryList> groceryLists= new ArrayList<>();
        for(HashMap.Entry<Integer, String> mapElement : groceryListNames.entrySet()) {
            int id = mapElement.getKey();
            groceryLists.add(getGroceryList(id, token));
        }
        return groceryLists;

    }


    @Override
    public GroceryList getGroceryList(long listID, String token) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder baseUrl = new HttpUrl.Builder().scheme("http").host("192.168.0.127").port(8080);
        HttpUrl url = baseUrl
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
        }catch (NullPointerException | IOException i) {
            return null;
        }
    }

    @Override
    public List<GroceryItem> getGroceryItems(long listID, String token) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder baseUrl = new HttpUrl.Builder().scheme("http").host("192.168.0.127").port(8080);
        HttpUrl url = baseUrl
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
            JSONObject jsonString = new JSONObject(Objects.requireNonNull(response.body()).string());
            JSONArray arr = jsonString.getJSONArray("items");

            // Create GroceryItem objects
            ArrayList<GroceryItem> groceryItems = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                String name = arr.getJSONObject(i).toString();
                GroceryItem groceryItem = new GroceryItem(name, listID);
                groceryItems.add(groceryItem);
            }
            return groceryItems;
        }catch (NullPointerException | IOException | JSONException i) {
            return null;
        }

    }

    @Override
    public GroceryList createGroceryList(String nameList, String token) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder baseUrl = new HttpUrl.Builder().scheme("http").host("192.168.0.127").port(8080);

        HttpUrl url = baseUrl
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
            return null;
        }
            return finalMapper.readValue(Objects.requireNonNull(response.body()).string(), GroceryList.class);
        }
        catch(NullPointerException | IOException e){
            return null;
        }
    }

    @Override
    public boolean setGroceryItems(List<String> items, long listID, String token) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder baseUrl = new HttpUrl.Builder().scheme("http").host("192.168.0.127").port(8080);

        HttpUrl url = baseUrl
                .addPathSegment("api")
                .addPathSegment("save-list")
                .build();

        HashMap<String, String> save = new HashMap<>();
        save.put("id", String.valueOf(listID));
        save.put("items", String.valueOf(items));
        try{
            // Create body
            RequestBody requestBody = RequestBody.create((new ObjectMapper()).writeValueAsString(save),
                    MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", token)
                    .put(requestBody)
                    .build();
            Response response = client.newCall(request).execute();

            return response.code() == HTTP_NO_CONTENT;

        } catch (IOException e) {
            return false;
        }

    }

    @Override
    public boolean deleteGroceryList(long listID, String token) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder baseUrl = new HttpUrl.Builder().scheme("http").host("192.168.0.127").port(8080);

        HttpUrl url = baseUrl
                .addPathSegment("api")
                .addPathSegment("delete-list")
                .addQueryParameter("id", String.valueOf(listID))
                .build();
        try{
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", token)
                    .delete()
                    .build();
            Response response = client.newCall(request).execute();

            return response.code() == HTTP_NO_CONTENT;
        } catch (IOException e) {
            return false;
        }
    }
}
