/*
 * UI Class
 */
package com.cactus;
import java.util.Objects;
import java.util.Scanner;

public class UI {
//    private GroceryListSystem groceryListSystem;

    public UI(){
//        this.groceryListSystem = new GroceryListSystem();
    }

    /**
     * Get user input from a list of options
     * @return  return int for user input
     */
    private int getInput() {
        System.out.println(Constants.INPUT_LINE);
        for (int i = 0; i < Constants.OPTIONS.length; i++) {
            System.out.println(i + ". " + Constants.OPTIONS[i]);
        }
        return this.getIntInput(0, Constants.OPTIONS.length-1);
    }

    /**
     * Get int console input from user between two given ints
     * @param min the minimum int that the user can input
     * @param max the maximum int that the user can input
     * @return the console input
     */
    private int getIntInput(int min, int max) {
        Scanner scanner = new Scanner(System.in);
        int input;
        do {
            System.out.println("Please enter a valid integer: ");
            input = scanner.nextInt();
        } while(min > input || input > max);
        return input;
    }

    /**
     * Get string console input from user
     * @return the string console input
     */
    private String getStringInput(String message) {
        Scanner scanner = new Scanner(System.in);
        String input = new String("");
        do {
            System.out.println(message);
            input = scanner.nextLine();
        } while(Objects.equals(input, ""));
        return input;
    }

    /**
     * Create new user
     * @return  if user was created
     */
    private boolean createUser() {
        String username = this.getStringInput(Constants.GET_USERNAME);
        String password = this.getStringInput(Constants.GET_PASSWORD);
        String name = this.getStringInput(Constants.GET_NAME);
//        return this.groceryListSystem.newUser(name, username, password);
        return true; // placeholder
    }

    /**
     * Delete given user
     */
    private boolean deleteUser() {
        String username = this.getStringInput(Constants.GET_USERNAME);
        String password = this.getStringInput(Constants.GET_PASSWORD);
//        return this.groceryListSystem.deleteUser(username, password);
        return true; // placeholder
    }

    /**
     * Log in user
     * @return  if user logged in
     */
    private boolean login() {
        String username = this.getStringInput(Constants.GET_USERNAME);
        String password = this.getStringInput(Constants.GET_PASSWORD);
//        return this.groceryListSystem.login(username, password);
        return true; // placeholder
    }

    /**
     * Create new grocery list
     * @return  if grocery list was added
     */
    private boolean createGroceryList() {
        String name = this.getStringInput(Constants.GET_NAME);
//        return this.groceryListSystem.newGroceryList(name);
        return true; // placeholder
    }

    /**
     * Add item to grocery list
     * @return  if the item was added to a category
     */
    private boolean addItem() {
        String item = this.getStringInput(Constants.GET_NAME);
//        return this.groceryListSystem.newItem(category, item);
        return true; // placeholder
    }

    /**
     * Display all grocery lists
     */
    private void displayGroceryLists() {
        // TODO: display grocery lists by calling method
    }

    /**
     * Display all grocery items for this user
     */
    private void displayGroceryItems() {
        System.out.println("Grocery List: ");
        // Call method from GroceryListSystem which returns a HashMap
//        String[] items = this.groceryListSystem.getGroceryListNames().values();
//        for (String item: items) {
//            System.out.println("| | " + item);
//        }
        System.out.println("Placeholder"); // placeholder
    }

    /**
     * Display recommendations
     */
    private void displayRecommendations() {
        // TODO: display recommendations by calling method
    }

    /**
     * Display header with current user and grocery list information
     */
    private void displayHeader() {
        System.out.println("Current User: "); // TODO: get name of user from GroceryListSystem
        System.out.println("Current grocery list: "); // TODO: get curr list from GroceryListSystem
    }

    /**
     * Run UI
     */
    public void run() {
        while(true) {
            this.displayHeader();
            int optionInput = this.getInput();
            if (optionInput == 0) { // new user
                boolean done = this.createUser();
                if (done) {
                    System.out.println("A new user was created.");
                } else {
                    System.out.println("This user already exists.");
                }
            } else if (optionInput == 1) { // log in
                boolean done = this.login();
                if (done) {
                    System.out.println("You have successfully logged in.");
                } else {
                    System.out.println("Sorry, you were not able to log in.");
                }
            } else if (optionInput == 2) { // new grocery list
                boolean done = this.createGroceryList();
                if (done) {
                    System.out.println("A new grocery list was successfully created.");
                } else {
                    System.out.println("Sorry, this grocery list was not able to be created.");
                }
            } else if (optionInput == 3) { // add grocery item
                boolean done = this.addItem();
                if (done) {
                    System.out.println("This item was added to the grocery list.");
                } else {
                    System.out.println("Sorry, this item was not able to be added.");
                }
            } else if (optionInput == 4) { // display grocery items
                this.displayGroceryItems();
            } else if (optionInput == 5) { // delete user
                boolean done = this.deleteUser();
                if (done) {
                    System.out.println("This user was deleted.");
                } else {
                    System.out.println("Sorry, the user was not able to be deleted.");
                }
            } else if (optionInput == 6) { // exit program
                return;
            }
        }
    }

}
