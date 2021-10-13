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
    public int getInput() {
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
    public int getIntInput(int min, int max) {
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
    public String getStringInput(String message) {
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
    public boolean createUser() {
        String name = this.getStringInput(Constants.GET_USERNAME);
        String password = this.getStringInput(Constants.GET_PASSWORD);
//        return this.groceryListSystem.newUser(name, password);
        return true; // placeholder
    }

    /**
     * Log in user
     * @return  if user logged in
     */
    public boolean login() {
        String name = this.getStringInput(Constants.GET_USERNAME);
        String password = this.getStringInput(Constants.GET_PASSWORD);
//        return this.groceryListSystem.login(name, password);
        return true; // placeholder
    }

    /**
     * Create new grocery list
     * @return  if grocery list was added
     */
    public boolean createGroceryList() {
        String name = this.getStringInput(Constants.GET_NAME);
//        return this.groceryListSystem.newGroceryList(name);
        return true; // placeholder
    }

    /**
     * Add item to grocery list
     * @return  if the item was added to a category
     */
    public boolean addItem() {
        String category = this.getStringInput(Constants.GET_CATEGORY);
        String item = this.getStringInput(Constants.GET_NAME);
//        return this.groceryListSystem.newItem(category, item);
        return true; // placeholder
    }

    /**
     * Display all grocery items for this user
     */
    public void displayGroceryItems() {
        System.out.println("Grocery List: ");
//        String[] items = this.groceryListSystem.getGroceryListNames().values();
//        for (String item: items) {
//            System.out.println("| | " + item);
//        }
        System.out.println("Placeholder"); // placeholder
    }

    /**
     * Run UI
     */
    public void run() {
        while(true) {
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
            } else if (optionInput == 5) { // exit program
                return;
            }
        }
    }

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        UI ui = new UI();
        ui.run();
    }
}
