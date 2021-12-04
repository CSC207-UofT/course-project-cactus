package com.cactus.exceptions;

import android.util.Log;

public class InternalException extends RuntimeException {

    public InternalException (Exception e) {
        super(e.getMessage(), e);
        Log.wtf(e.getStackTrace()[0].getClassName(), e.getMessage(), e);
        e.printStackTrace();
    }
}
