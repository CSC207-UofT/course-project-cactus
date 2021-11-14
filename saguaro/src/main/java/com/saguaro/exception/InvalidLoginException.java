package com.saguaro.exception;

/**
 * Exception thrown when invalid login credentials are recieved.
 */
public class InvalidLoginException extends Exception {

    public InvalidLoginException() {
        super("Invalid username/password");
    }
}
