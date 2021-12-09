package com.cactus.adapters;

import com.cactus.entities.GroceryList;
import com.cactus.exceptions.InternalException;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.cactus.adapters.HttpUtil.makeRequest;
import static com.cactus.adapters.HttpUtil.writeMapAsBody;

/**
 * The WebGroceryAdapter class implements the GroceryAdapter Interface by interacting with a server
 * that stores our Users and Grocery Lists.
 */
public class WebGroceryAdapter implements GroceryAdapter {

    private final String STATIC_IP;

    private final OkHttpClient client;

    @Inject
    public WebGroceryAdapter(OkHttpClient client) {
        String tempIp = "192.168.0.127"; // default to this address
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream input = Objects.requireNonNull(classloader).getResourceAsStream("network.properties");

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
     * Returns a List of GroceryList objects that correspond to the User associated with the given token
     * <p>
     * <p>
     * The token is sent to the server to return the list of grocery lists, which returns the names and ids.
     * <p>
     * The id of each list is used to get every grocery list using getGroceryList().
     *
     * @param token a string representing the user token to fetch lists for
     * @return a List of GroceryList objects that are of the User whose token is entered
     */
    @Override
    public List<GroceryList> getGroceryListNamesByUser(String token) throws InvalidParamException, ServerException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(STATIC_IP)
                .port(8080)
                .addPathSegment("api")
                .addPathSegment("v2")
                .addPathSegment("all-lists").build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .build();

        String responseString = makeRequest(this.client, request);

        Map<String,
                Map<String,
                        Map<Long, String>>> fullListNames;

        try {
            fullListNames = new ObjectMapper()
                    .readValue(responseString, new TypeReference<Map<String, Map<String, Map<Long, String>>>>() {
                    });
            // TypeReference uses same type as fullListNames
        } catch (IOException e) {
            throw new InternalException(e);
        }

        // O(1) appending
        int size = Objects.requireNonNull(Objects.requireNonNull(fullListNames.get("lists")).get("owned")).size() +
                Objects.requireNonNull(Objects.requireNonNull(fullListNames.get("lists")).get("shared")).size() +
                Objects.requireNonNull(Objects.requireNonNull(fullListNames.get("templates")).get("owned")).size() +
                Objects.requireNonNull(Objects.requireNonNull(fullListNames.get("templates")).get("shared")).size();
        List<GroceryList> result = new ArrayList<>(size);

        this.parse(Objects.requireNonNull(Objects.requireNonNull(fullListNames.get("lists")).get("owned")),
                false, result);
        this.parse(Objects.requireNonNull(Objects.requireNonNull(fullListNames.get("lists")).get("shared")),
                false, result);

        this.parse(Objects.requireNonNull(Objects.requireNonNull(fullListNames.get("templates")).get("owned")),
                true, result);
        this.parse(Objects.requireNonNull(Objects.requireNonNull(fullListNames.get("templates")).get("shared")),
                true, result);

        return result;
    }

    private void parse(Map<Long, String> lists, boolean template, List<GroceryList> result) {
        for (Map.Entry<Long, String> entry : lists.entrySet()) {
            GroceryList list = new GroceryList();
            list.setId(entry.getKey());
            list.setName(entry.getValue());
            list.setTemplate(template);

            result.add(list);
        }
    }

    /**
     * Returns a list of GroceryItem objects that belong to the given list
     * <p>
     * The token and listID are sent to the server to return the list of Grocery Items.
     * <p>
     * If the listID does not correspond to an existing list, then null is returned.
     *
     * @param listID id of the list which holds the items you are getting
     * @param token  token of the user who holds the list
     * @return a list of GroceryItems in the list
     */
    @Override
    public GroceryList getGroceryList(long listID, String token) throws InvalidParamException, ServerException {
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

        String responseBody = makeRequest(this.client, request, "Grocery list could not be found");

        try {
            return new ObjectMapper()
                    .readValue(responseBody, GroceryList.class);
        } catch (JsonProcessingException e) {
            throw new InternalException(e);
        }
    }

    /**
     * Returns the username of the Owner of the grocery list.
     * <p>
     * The token and listID are sent to the server to return the GroceryList, from which the owner is retrieved.
     * <p>
     * If the listID does not correspond to an existing list, then null is returned.
     *
     * @param listID id of the list which holds the items you are getting
     * @param token  token of the user who holds the list
     * @return the username of the Owner of the Grocery List
     */
    @Override
    public String getGroceryListOwner(long listID, String token) throws InvalidParamException, ServerException {
        GroceryList groceryList = getGroceryList(listID, token);
        return groceryList.getOwner();

    }

    /**
     * Create a GroceryList with the name given. A user token is required for authorization.
     * <p>
     * nameList and token are sent to the server to create a GroceryList.
     * <p>
     * If the request fails, return null.
     * <p>
     * If a list should not be initialized with a template, provide a templateId of -1.
     *
     * @param nameList   a String containing the name of the new grocery list
     * @param token      a string representing the token of the user creating the
     *                   list
     * @param template   a boolean specifying if the created list should be a template
     * @param templateId a long representing the template ID to initialize this list with
     * @return a GroceryList that corresponds to the GroceryList created
     */
    @Override
    public GroceryList createGroceryList(String nameList, String token, boolean template, long templateId) throws InvalidParamException, ServerException {
        HttpUrl.Builder urlPart = new HttpUrl.Builder()
                .scheme("http")
                .host(STATIC_IP)
                .port(8080)
                .addPathSegment("api")
                .addPathSegment("create-list")
                .addQueryParameter("name", nameList)
                .addQueryParameter("template", Boolean.toString(template));

        HttpUrl url;
        if (templateId > -1) {
            url = urlPart.addQueryParameter("templateId", String.valueOf(templateId))
                    .build();
        } else {
            url = urlPart.build();
        }

        // Create body
        RequestBody requestBody = RequestBody.create("",
                MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .post(requestBody)
                .build();

        String responseBody = makeRequest(this.client, request, "Cannot create list with these settings");

        try {
            return new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(responseBody, GroceryList.class);

        } catch (JsonProcessingException e) {
            throw new InternalException(e);
        }
    }

    /**
     * Returns whether the list of grocery items are added to the GroceryList that corresponds
     * to the listID given. A user token is required for authorization.
     * <p>
     * The items, listID and token are sent to the server to append items to the list corresponding to
     * listID.
     *
     * @param items  a List of Strings containing the names of grocery items to set
     * @param listID a long representing the ID of the list to change
     * @param token  a string representing the token of the list's owner
     */
    @Override
    public void setGroceryItems(List<String> items, long listID, String token) throws InvalidParamException, ServerException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(STATIC_IP)
                .port(8080)
                .addPathSegment("api")
                .addPathSegment("save-list")
                .build();

        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("id", listID);
        bodyMap.put("items", items);

        RequestBody requestBody = writeMapAsBody(bodyMap);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .put(requestBody)
                .build();

        makeRequest(this.client, request, "Could not save to this list");

    }

    /**
     * Returns whether the GroceryList corresponding to the listID is deleted.
     * A user token is required for authorization.
     * <p>
     * The listID and token are sent to the server to delete the list corresponding to listID.
     *
     * @param listID a long representing the ID of the grocery list to delete
     * @param token  a string representing the token of the grocery list's owner
     */
    @Override
    public void deleteGroceryList(long listID, String token) throws InvalidParamException, ServerException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(STATIC_IP)
                .port(8080)
                .addPathSegment("api")
                .addPathSegment("delete-list")
                .addQueryParameter("id", String.valueOf(listID))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .delete()
                .build();

        makeRequest(this.client, request, "Could not delete list");
    }

    /**
     * Shares the List belonging to id to user represented by username.
     * <p>
     * The id and token are sent to the server to share the list corresponding to listID.
     *
     * @param id       a long representing the ID of the grocery list to share
     * @param username The username of the User the list is being shared with
     * @param token    a string representing the token of the grocery list's owner
     */
    @Override
    public void shareList(long id, String username, String token) throws InvalidParamException, ServerException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(STATIC_IP)
                .port(8080)
                .addPathSegment("api")
                .addPathSegment("share-list")
                .addQueryParameter("id", String.valueOf(id))
                .addQueryParameter("username", username)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .post(RequestBody.create(new byte[0], null))
                .build();

        makeRequest(this.client, request, "Lists can only be shared with friends");
    }

    /**
     * Unshares the List belonging to id to user represented by username.
     * <p>
     * The id and token are sent to the server to unshare the list corresponding to listID.
     *
     * @param id       a long representing the ID of the grocery list to share
     * @param username The username of the User the list is being shared with
     * @param token    a string representing the token of the grocery list's owner
     */
    @Override
    public void unshareList(long id, String username, String token) throws InvalidParamException, ServerException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(STATIC_IP)
                .port(8080)
                .addPathSegment("api")
                .addPathSegment("unshare-list")
                .addQueryParameter("id", String.valueOf(id))
                .addQueryParameter("username", username)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .delete()
                .build();

        makeRequest(this.client, request);
    }
}
