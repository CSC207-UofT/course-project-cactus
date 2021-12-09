package com.cactus.adapters;

import com.cactus.exceptions.InternalException;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class HttpUtil {

    public static boolean is4xx(int code) {
        return 400 <= code && code < 500;
    }

    public static boolean is5xx(int code) {
        return 500 <= code && code < 600;
    }

    public static String makeRequest(OkHttpClient client, Request request) throws InvalidParamException, ServerException {
        return makeRequest(client, request, null);
    }

    public static String makeRequest(OkHttpClient client, Request request, String is4xxMessage) throws ServerException, InvalidParamException {
        Response response;

        try {
            response = client.newCall(request).execute();

        } catch (IOException e) {
            throw new ServerException(e.getMessage());
        }

        if (is5xx(response.code())) {
            try {
                throw new ServerException(response + "\n" + Objects.requireNonNull(response.body()).string());
            } catch (IOException e) {
                // doubly bad
                throw new InternalException(e);
            }
        }

        if (is4xx(response.code())) {
            String body;
            try {
                body = Objects.requireNonNull(response.body()).string();
            } catch (IOException e) {
                // doubly bad
                throw new InternalException(e);
            }

            // return server message if no dedicated message is specified
            if (is4xxMessage == null) {
                throw new InvalidParamException(body, response + "\n" + body);
            } else {
                throw new InvalidParamException(is4xxMessage, response + "\n" + body);
            }
        }

        try {
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            throw new InternalException(e);
        }
    }

    public static RequestBody writeMapAsBody(Map<?, ?> map) {
        try {
            return RequestBody.create((new ObjectMapper()).writeValueAsString(map),
                    MediaType.get("application/json; charset=utf-8"));
        } catch (JsonProcessingException e) {
            throw new InternalException(e);
        }
    }
}
