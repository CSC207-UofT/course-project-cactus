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

/**
 * The WebGroceryAdapter class implements the GroceryAdapter Interface by interacting with a server
 * that stores our Users and Grocery Lists.
 */
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


    /**
     * Returns a List of GroceryList objects that correspond to the User associated with the given token
     * <p>
     *
     * The token is sent to the server to return the list of grocery lists, which returns the names and ids.
     *
     * The id of each list is used to get every grocery list using getGroceryList().
     *
     *
     * @param token a string representing the user token to fetch lists for
     * @return a List of GroceryList objects that are of the User whose token is entered
     */
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

    /**
     * Returns a GroceryList corresponding to the listID given.
     * A user token is required for authorization.
     * <p>
     * The token and listID are sent to the server to return the GroceryList.
     *
     * If the listID does not correspond to an existing list, then null is returned.
     *
     * @param listID a long representing the ID of the list to get
     * @param token  a string representing the token of the user the list belongs to
     * @return a GroceryList corresponding to the listID given
     */
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

    /**
     * Returns a list of GroceryItem objects that belong to the given list
     *
     * The token and listID are sent to the server to return the list of Grocery Items.
     *
     * If the listID does not correspond to an existing list, then null is returned.
     *
     * @param listID id of the list which holds the items you are getting
     * @param token  token of the user who holds the list
     * @return a list of GroceryItems in the list
     */
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

    /**
     * Returns a GroceryList with the name given. A user token is required for authorization.
     *
     * nameList and token are sent to the server to create a GroceryList.
     *
     * If the token does not correspond to the User, null is returned.
     *
     * @param nameList a String containing the name of the new grocery list
     * @param token    a string representing the token of the user creating the
     *                 list
     * @return a GroceryList that corresponds to the GroceryList created
     */
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

    /**
     * Returns whether the list of grocery items are added to the GroceryList that corresponds
     * to the listID given. A user token is required for authorization.
     *
     * The items, listID and token are sent to the server to append items to the list corresponding to
     * listID.
     *
     * @param items  a List of Strings containing the names of grocery items to set
     * @param listID a long representing the ID of the list to change
     * @param token  a string representing the token of the list's owner
     * @return a boolean indicating whether the grocery items are appended to the list
     */
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

    /**
     * Returns whether the GroceryList corresponding to the listID is deleted.
     * A user token is required for authorization.
     *
     *The listID and token are sent to the server to delete the list corresponding to listID.
     *
     *
     * @param listID a long representing the ID of the grocery list to delete
     * @param token  a string representing the token of the grocery list's owner
     * @return a Response to the grocery list deletion operation
     */
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
