package com.cactus.adapters;

import com.cactus.exceptions.InternalException;
import com.cactus.exceptions.InvalidParamException;
import com.cactus.exceptions.ServerException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class HttpUtil {

    public final static int HTTP_OK = 200;
    public final static int HTTP_NO_CONTENT = 204;

    public static boolean is2xx(int code) {
        return 200 <= code && code < 300;
    }

    public static boolean is3xx(int code) {
        return 300 <= code && code < 400;
    }

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
                throw new ServerException(response + "\n" + response.body().string());
            } catch (IOException e) {
                // doubly bad
                throw new InternalException(e);
            }
        }

        if (is4xx(response.code())) {
            String body = "";
            try {
                body = response.body().string();
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
            return response.body().string();
        } catch (IOException e) {
            throw new InternalException(e);
        }
    }
}
