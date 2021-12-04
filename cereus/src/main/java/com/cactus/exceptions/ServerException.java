package com.cactus.exceptions;

/**
 * This class defines an exception thrown whenever a 5xx server response is returned.
 */
public class ServerException extends Exception {

    /**
     * Constructs an InvalidParamException, with a message to log for debugging.
     *
     * @param logMessage a String message intended for logs
     */
    public ServerException(String logMessage) {
        super(logMessage);
    }

    /**
     * Get a nicely formatted exception message intended to be displayed as a Toast to the user.
     *
     * Specifically, a user does not need to know specifics about server errors, so a try again
     * is returned.
     *
     * @return an exception message for the user
     */
    public String getToastMessage() {
        return "Something went wrong. Please try again later.";
    }
}
