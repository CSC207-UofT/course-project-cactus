package com.cactus.adapters;

import com.cactus.data.EntityRepository;
import com.cactus.entities.GroceryItem;
import com.cactus.entities.GroceryList;
import com.cactus.entities.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/***
 * Represents ClassGroceryAdapter which implements GroceryAdapter interface.
 */

public class ClassGroceryAdapter implements GroceryAdapter {

    private EntityRepository repository;

    public ClassGroceryAdapter(EntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Response getGroceryListsByUser(long userid) {
        User user = repository.getUserById(userid);

        if (user != null) {
            GroceryList[] lists = repository.getGroceryListByUser(user).toArray(new GroceryList[0]);

            // payload includes: length, [x] where x is index for grocery list id
            HashMap<String, String> payload = new HashMap<>();
            payload.put("length", String.valueOf(lists.length));

            for (int i = 0; i < lists.length; i++) {
                payload.put(String.valueOf(i), String.valueOf(lists[i].getId()));
            }

            return new Response(Response.Status.OK, payload);
        } else {
            return new Response(Response.Status.NOT_FOUND);
        }
    }

    @Override
    public Response getGroceryList(long listid, long userid) {
        User user = repository.getUserById(userid);

        if (user != null) {

            GroceryList target = null;

            Collection<GroceryList> lists = repository.getGroceryListByUser(user);
            for (GroceryList list : lists) {
                if (list.getId() == listid) {
                    target = list;
                }
            }

            if (target != null) {
                GroceryItem[] items = repository.getGroceryItemsByList(target).toArray(new GroceryItem[0]);

                // payload includes: listid, name, length,
                // [x] where x is index for grocery item name
                HashMap<String, String> payload = new HashMap<>();
                payload.put("listid", String.valueOf(target.getId()));
                payload.put("name", target.getName());
                payload.put("length", String.valueOf(items.length));

                for (int i = 0; i < items.length; i++) {
                    payload.put(String.valueOf(i), items[i].getName());
                }

                return new Response(Response.Status.OK, payload);

            } else {
                return new Response(Response.Status.NOT_FOUND);
            }

        } else {
            return new Response(Response.Status.NOT_FOUND);
        }

    }

    @Override
    public Response createGroceryList(String nameList, long userid) {
        User user = repository.getUserById(userid);

        if (user != null) {
            GroceryList list = new GroceryList(nameList);
            list.addUserId(userid);
            if (repository.saveGroceryList(list)) {

                // payload includes: listid, name
                HashMap<String, String> payload = new HashMap<>();
                payload.put("listid", String.valueOf(list.getId()));
                payload.put("name", list.getName());

                return new Response(Response.Status.OK, payload);
            } else {
                return new Response(Response.Status.BAD_REQUEST);
            }

        } else {
            return new Response(Response.Status.BAD_REQUEST);
        }
    }

    @Override
    public Response setGroceryItems(List<String> items, long listid, long userid) {
        User user = repository.getUserById(userid);

        if (user != null) {

            GroceryList target = null;

            Collection<GroceryList> lists = repository.getGroceryListByUser(user);
            for (GroceryList list : lists) {
                if (list.getId() == listid) {
                    target = list;
                }
            }

            if (target != null) {
                GroceryItem[] existingItems = repository.getGroceryItemsByList(target).toArray(new GroceryItem[0]);
                ArrayList<String> inputItems = new ArrayList<String>(items);

                for (GroceryItem item : existingItems) {
                    if (!inputItems.contains(item.getName())) {
                        repository.deleteGroceryItem(item.getId());
                    } else {
                        inputItems.remove(item.getName());
                    }
                }

                for (String itemName : inputItems) {
                    GroceryItem item = new GroceryItem(itemName, target.getId());
                    repository.saveGroceryItem(item);
                }

                return new Response(Response.Status.NO_CONTENT);

            } else {
                return new Response(Response.Status.BAD_REQUEST);
            }

        } else {
            return new Response(Response.Status.BAD_REQUEST);
        }
    }

    @Override
    public Response deleteGroceryList(long listid, long userid) {
        User user = repository.getUserById(userid);

        if (user != null) {
            Collection<GroceryList> lists = repository.getGroceryListByUser(user);
            List<Long> listIds = lists.stream().map(GroceryList::getId).collect(Collectors.toList());

            if (listIds.contains(listid)) {
                repository.deleteGroceryList(listid);

                return new Response(Response.Status.NO_CONTENT);

            } else {
                return new Response(Response.Status.BAD_REQUEST);
            }
        } else {
            return new Response(Response.Status.BAD_REQUEST);
        }
    }
}
