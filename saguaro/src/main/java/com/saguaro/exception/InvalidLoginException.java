package com.saguaro.exception;

/**
 * Exception thrown when invalid login credentials are received.
 *
 * @author Charles Wong
 */
public class InvalidLoginException extends Exception {

    /**
     * Construct an InvalidLoginException with the following default message:
     * "Invalid username/password"
     */
    public InvalidLoginException() {
        super("Invalid username/password");
    }
}
