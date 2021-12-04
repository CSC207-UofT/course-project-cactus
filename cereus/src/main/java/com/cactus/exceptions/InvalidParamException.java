package com.cactus.exceptions;

/**
 * Exception thrown when some invalid parameter is provided
 */
public class InvalidParamException extends Exception {

    /**
     * Message to display to user
     */
    String toastMessage;

    /**
     * Constructs an InvalidParamException, with a message to display as a Toast
     * to the user, and a message to log for debugging.
     *
     * @param toastMessage a String message intended for the user
     * @param logMessage a String message intended for logs
     */
    public InvalidParamException(String toastMessage, String logMessage) {
        super(logMessage);
        this.toastMessage = toastMessage;
    }

    /**
     * Get a nicely formatted exception message intended to be displayed as a Toast to the user
     *
     * @return an exception message for the user
     */
    public String getToastMessage() {
        return this.toastMessage;
    }
}
