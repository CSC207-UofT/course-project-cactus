package com.cactus.adapters;

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
}
