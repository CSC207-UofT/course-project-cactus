package com.saguaro.exception;

import com.saguaro.entity.User;

/**
 * An exception thrown when attempting to perform some operation on an entity that
 * does not exist.
 *
 * @author Charles Wong
 */
public class ResourceNotFoundException extends Exception {

    /**
     * Constructs a exception with some custom message.
     * <p>
     * User this when the templated messages provided by the alternative
     * constructors are not sufficient.
     *
     * @param msg the String message to initialize this exception with
     */
    public ResourceNotFoundException(String msg) {
        super(msg);
    }

    /**
     * Constructor for this exception that will initialize it with a message detailing
     * what was not found
     * <p>
     * Use this when the resource not found is not linked with any specific user.
     *
     * @param type the Class of the entity not found
     * @param id   a String representation of the ID of the entity that was attempted
     *             to be searched for
     */
    public ResourceNotFoundException(Class<?> type, String id) {
        super(constructMessage(type, id));
    }

    /**
     * Constructor for this exception that will initialize it with a message detailing
     * what was not found
     *
     * @param type the Class of the entity not found
     * @param id   a String representation of the ID of the entity that was attempted
     *             to be searched for
     * @param user the User that the entity was not found for
     */
    public ResourceNotFoundException(Class<?> type, String id, User user) {
        super(constructMessage(type, id, user));
    }

    /**
     * Generate an exception message given the type that was not found, the ID that was
     * not found, and the User that the entity was not found for.
     *
     * @param type the Class of the entity not found
     * @param id   a String representation of the ID of the entity that was attempted
     *             to be searched for
     * @param user the User that the entity was not found for
     * @return a formatted message detailing what was not found
     */
    private static String constructMessage(Class<?> type, String id, User user) {
        return "Could not find " + type.getSimpleName() + " " + id + " for user " + user.getUsername();
    }

    /**
     * Generate an exception message given the type that was not found and the ID that
     * was not found. This message is intended for non-existent resources not belonging
     * to any specific user.
     *
     * @param type the Class of the entity not found
     * @param id   a String representation of the ID of the entity that was attempted
     *             to be searched for
     * @return a formatted message detailing what was not found
     */
    private static String constructMessage(Class<?> type, String id) {
        return "Could not find " + type.getSimpleName() + " " + id;
    }
}
