package com.cactus.exceptions;

/**
 * This class defines an exception thrown whenever a 5xx server response is returned, or
 * when a request fails.
 */
public class ServerException extends CereusException {

    /**
     * Constructs an InvalidParamException, with a message to log for debugging.
     *
     * @param logMessage a String message intended for logs
     */
    public ServerException(String logMessage) {
        super(null, logMessage);
    }

    /**
     * Get a nicely formatted exception message intended to be displayed as a Toast to the user.
     * <p>
     * Specifically, a user does not need to know specifics about server errors, so a try again
     * is returned.
     *
     * @return an exception message for the user
     */
    @Override
    public String getToastMessage() {
        return "Something went wrong. Please try again later.";
    }
}
