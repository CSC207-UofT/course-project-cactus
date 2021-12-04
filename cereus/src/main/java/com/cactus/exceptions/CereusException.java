package com.cactus.exceptions;

public abstract class CereusException extends Exception {

    /**
     * Message to display to user
     */
    private final String toastMessage;

    public CereusException(String toastMessage, String logMessage) {
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
