package com.saguaro.exception;

/**
 * Exception thrown when the parameters received by a method are in some way
 * invalid.
 * <p>
 * A similar exception exists as part of java.security, however that implementation
 * is an unchecked exception.
 */
public class InvalidParamException extends Exception {

    /**
     * Construct an InvalidParamException with some message string.
     *
     * @param message the String message to attach to this exception
     */
    public InvalidParamException(String message) {
        super(message);
    }
}
