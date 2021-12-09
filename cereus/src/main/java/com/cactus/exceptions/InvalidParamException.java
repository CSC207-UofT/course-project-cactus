package com.cactus.exceptions;

/**
 * Exception thrown when some invalid parameter is provided
 */
public class InvalidParamException extends CereusException {

    /**
     * Constructs an InvalidParamException, with a message to display as a Toast
     * to the user, and a message to log for debugging.
     *
     * @param toastMessage a String message intended for the user
     * @param logMessage   a String message intended for logs
     */
    public InvalidParamException(String toastMessage, String logMessage) {
        super(toastMessage, logMessage);
    }
}
