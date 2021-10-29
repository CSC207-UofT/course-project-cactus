/*
 * UI Class
 */
package com.cactus.ui;
import com.cactus.systems.GroceryListSystem;
import com.cactus.systems.UserInteractFacade;
import com.cactus.systems.UserSystem;

import java.util.*;

public class UI {
    private final UserInteractFacade userInteractFacade;
    private int curr_state;

    /**
     * Initialize UI
     * @param userInteractFacade the user interaction facede for running this instance
     */
    public UI(UserInteractFacade userInteractFacade){
        this.userInteractFacade = userInteractFacade;
        this.curr_state = Constants.LOGIN_STATE;
    }

    /**
     * Get user input from a list of options
     * @return  return int for user input
     */
    private int getInput(int[] options) {
        System.out.println(Constants.INPUT_LINE);
        for (int option: options){
            System.out.println("[" + option + "] " + Constants.INPUT_OPTIONS.get(option));
        }
        return this.getIntInput(options);
    }

    /**
     * Get int console input from user between two given ints
     * @param options the list of int options
     * @return the console input
     */
    private int getIntInput(int[] options) {
        Scanner scanner = new Scanner(System.in);
        int input;
        while(true) {
            System.out.println("Please enter a valid integer: ");
            input = scanner.nextInt();
            for (int option : options) {
                if (input == option) {
//                    scanner.close();
                    return input;
                }
            }
        }
    }

    /**
     * Get string console input from user
     * @return the string console input
     */
    private String getStringInput(String message) {
        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            System.out.println(message);
            input = scanner.nextLine();
        } while(Objects.equals(input, ""));
//        scanner.close();
        return input;
    }

    /**
     * Outputs response to user after action is done
     * @param done  if action is done
     */
    private void respond(boolean done) {
        if (done) {
            System.out.println("Success!");
        } else{
            System.out.println("Sorry, the requested action was unable to be processed.");
        }
    }

    /**
     * Create new user
     * @return  if user was created
     */
    private boolean createUser() {
        String username = this.getStringInput(Constants.GET_USERNAME);
        String password = this.getStringInput(Constants.GET_PASSWORD);
        String name = this.getStringInput(Constants.GET_NAME);

        return this.userInteractFacade.createUser(name, username, password);
    }

    /**
     * Delete given user
     */
    private boolean deleteUser() {
        String username = this.getStringInput(Constants.GET_USERNAME);
        String password = this.getStringInput(Constants.GET_PASSWORD);
//        return this.groceryListSystem.deleteUser(username, password);
        return true; // TODO: functionality does not exist in AuthAdapter
    }

    /**
     * Log in user
     * @return  if user logged in
     */
    private boolean login() {
        String username = this.getStringInput(Constants.GET_USERNAME);
        String password = this.getStringInput(Constants.GET_PASSWORD);
        return this.userInteractFacade.login(username, password);
    }

    /**
     * Log out user
     * @return  if user logged out
     */
    private boolean logout() {
//        this.userSystem.logout(); // TODO: functionality does not exist in AuthAdapter
        return false;
    }

    /**
     * Create new grocery list
     * @return  if grocery list was added
     */
    private boolean createGroceryList() {
        String name = this.getStringInput(Constants.GET_NAME);

        return this.userInteractFacade.newGroceryList(name);
    }

    /**
     * Add item to grocery list
     * @return  if the item was added to a category
     */
    private boolean addItem() {
        String item = this.getStringInput(Constants.GET_NAME);

        return this.userInteractFacade.addGroceryItem(item);
    }

    /**
     * Exit current list to list of grocery lists
     */
    private boolean exitList() {
        this.userInteractFacade.exitGroceryList();
        return true;
    }

    /**
     * Exit current list to list of grocery lists
     */
    private boolean deleteList() {
        this.userInteractFacade.deleteGroceryList();
        return true;
    }

    /**
     * Display all grocery lists
     */
    private void displayGroceryLists() {
        List<String> lists = this.userInteractFacade.getGroceryListNames();
        if (lists.size() == 0) {
            System.out.println("You have no lists!");
        } else {
            for (String name : lists) {
                System.out.println("--" + name + "--");
            }
        }
    }

    /**
     * Display all grocery items for this user
     */
    private void displayGroceryItems() {
        ArrayList<String> items = this.userInteractFacade.getGroceryItemNames();
        if (items.size() == 0) {
            System.out.println("You have no items in this list!");
        } else {
            for (String name:items) {
                System.out.println("| | " + name);
            }
        }
    }

    /**
     * Display header with current user and grocery list information
     */
    private void displayHeader() {
        System.out.println("Current User: " + this.userInteractFacade.getUserName());
        System.out.println("Current grocery list: " + this.userInteractFacade.getListName());
    }

    /**
     * Run UI
     */
    public void run() {
        int optionInput;
        boolean done = false;
        while(true) {
//            this.displayHeader();

            // get input option
            if (curr_state == Constants.LOGIN_STATE) {
                optionInput = this.getInput(Constants.LOGIN_OPTIONS);
            } else if (curr_state == Constants.GROCERY_LISTS_STATE) {
                optionInput = this.getInput(Constants.LISTS_OPTIONS);
            } else {
                optionInput = this.getInput(Constants.ITEMS_OPTIONS);
            }

            // execute command
            if (optionInput == Constants.NEW_USER) {
                done = this.createUser();
                if (done) {this.curr_state = Constants.GROCERY_LISTS_STATE;}
            } else if (optionInput == Constants.LOGIN) {
                done = this.login();
                if (done) {this.curr_state = Constants.GROCERY_LISTS_STATE;}
            } else if (optionInput == Constants.NEW_LIST) {
                done = this.createGroceryList();
                if (done) {this.curr_state = Constants.GROCERY_ITEMS_STATE;}
            } else if (optionInput == Constants.NEW_ITEM) {
                done = this.addItem();
            } else if (optionInput == Constants.ITEMS) {
                this.displayGroceryItems();
                done = true;
            } else if (optionInput == Constants.LISTS) {
                this.displayGroceryLists();
                done = true;
            } else if (optionInput == Constants.CHOOSE_LIST) {
                done = false;
                // TODO: implement choosing a list in controller
            } else if (optionInput == Constants.DELETE_USER) {
                done = this.deleteUser();
                if (done) {this.curr_state = Constants.LOGIN_STATE;}
            } else if (optionInput == Constants.LOGOUT) {
                done = this.logout();
                this.curr_state = Constants.LOGIN_STATE;
            } else if (optionInput == Constants.EXIT_LIST) {
                done = this.exitList();
                if (done) {this.curr_state = Constants.GROCERY_LISTS_STATE;}
            } else if (optionInput == Constants.DELETE_LIST) {
               done = this.deleteList();
               if (done) {this.curr_state = Constants.GROCERY_LISTS_STATE;}
            } else if (optionInput == Constants.QUIT) {
                return;
            }

            this.respond(done);
        }
    }

}
