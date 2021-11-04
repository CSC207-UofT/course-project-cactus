package com.cactus.adapters;

import java.util.HashMap;
import java.util.Objects;

/**
 * Response class for actions
 */
public class Response {

    private final Status code;
    private final HashMap<String, String> payload;

    public enum Status {
        OK (200),
        NO_CONTENT (204),
        BAD_REQUEST (400),
        NOT_FOUND (404),
        ERROR (500);

        private final int code;

        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public Response(Status code) {
        this.code = code;
        this.payload = null;
    }

    public Response(Status code, HashMap<String, String> payload) {
        this.code = code;
        this.payload = payload;
    }

    public Status getStatusCode() {
        return this.code;
    }

    public HashMap<String, String> getPayload() {
        return this.payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Response response = (Response) o;
        return code == response.code && Objects.equals(payload, response.payload);
    }
}