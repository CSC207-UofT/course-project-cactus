package com.saguaro.exception;

import com.saguaro.entity.GroceryItem;
import com.saguaro.entity.GroceryList;
import com.saguaro.entity.User;

public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException(Class<?> type, String id, User user) {
        super(constructMessage(type, id, user));
    }

    private static String constructMessage(Class<?> type, String id, User user) {
        String messageType;

        if (type.equals(GroceryItem.class)) {
            messageType = "grocery item";
        } else if (type.equals(GroceryList.class)) {
            messageType = "grocery list";
        } else {
            messageType = "resource";
        }

        return "Could not find " + messageType + " " + id + " for user " + user.getUsername();
    }
}
