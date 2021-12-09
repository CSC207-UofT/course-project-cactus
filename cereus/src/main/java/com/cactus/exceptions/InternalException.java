package com.cactus.exceptions;

import android.util.Log;

/**
 * Extremely bad things have happened if these get thrown, so we force a big red
 * log and print a stack trace
 */
public class InternalException extends RuntimeException {

    public InternalException(Exception e) {
        super(e.getMessage(), e);
        Log.wtf(e.getStackTrace()[0].getClassName(), e.getMessage(), e);
        e.printStackTrace();
    }
}
