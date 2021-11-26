package com.saguaro.exception;

import com.saguaro.entity.User;

public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException(Class<?> type, String id) {
        super(constructMessage(type, id));
    }

    public ResourceNotFoundException(Class<?> type, String id, User user) {
        super(constructMessage(type, id));
    }

    private static String constructMessage(Class<?> type, String id, User user) {
        return "Could not find " + type.getSimpleName() + " " + id + " for user " + user.getUsername();
    }

    private static String constructMessage(Class<?> type, String id) {
        return "Could not find " + type.getSimpleName() + " " + id;
    }
}
