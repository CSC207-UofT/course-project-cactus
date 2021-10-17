package com.cactus.ui;

import java.util.Map;
import static java.util.Map.entry;

public class Constants {
    public static final int NEW_USER = 0;
    public static final int LOGIN = 1;
    public static final int NEW_LIST = 2;
    public static final int NEW_ITEM = 3;
    public static final int ITEMS = 4;
    public static final int LISTS = 5;
    public static final int DELETE_USER = 6;
    public static final int LOGOUT = 7;
    public static final int QUIT = 8;
    public static final int CHOOSE_LIST = 9;
    public static final int EXIT_LIST = 10;

    public static final String GET_NAME = "Please enter a name: ";
    public static final String GET_USERNAME = "Please enter a username: ";
    public static final String GET_PASSWORD = "Please enter a password: ";
    public static final String INPUT_LINE = "Please specify your action: ";

    public static final Map<Integer, String> INPUT_OPTIONS = Map.ofEntries(
            entry(NEW_USER, "Create new user"),
            entry(LOGIN, "Log in"),
            entry(NEW_LIST, "Create new list"),
            entry(CHOOSE_LIST, "Select a list"),
            entry(NEW_ITEM, "Add item to list"),
            entry(ITEMS, "Display grocery items"),
            entry(LISTS, "Display all grocery lists"),
            entry(DELETE_USER, "Delete user"),
            entry(LOGOUT, "Log out"),
            entry(QUIT, "Exit"),
            entry(EXIT_LIST, "Exist current list"));

    public static final int[] LOGIN_OPTIONS = {NEW_USER, LOGIN, QUIT};
    public static final int[] LISTS_OPTIONS = {NEW_LIST, CHOOSE_LIST, LISTS, LOGOUT, QUIT};
    public static final int[] ITEMS_OPTIONS = {NEW_ITEM, ITEMS, EXIT_LIST, LOGOUT, QUIT};

    public static final int LOGIN_STATE = 0;
    public static final int GROCERY_LISTS_STATE = 1;
    public static final int GROCERY_ITEMS_STATE = 2;
}
