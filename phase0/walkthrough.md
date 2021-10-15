## Scenario Walk-through

#### Having a new user create a grocery list:

First, the user will be greeted by the console asking them to create an account with their choice of username, name, and password. 
The user enters the required info, and the UI class will ask the GroceryListSystem class to create a user. 
The GroceryListSystem class communicates with the UserManager to create the new user, and the GroceryListSystem stores this in the EntityRepository. 
This will automatically login the new user.
With the user logged in, the UI will display all grocery lists for this user. They will be prompted to logout, create a new grocery list, or select an existing grocery list.
If the user does not have an existing list, this option will be hidden.

The user chooses to create a new grocery list, which will ask the user for the name of the grocery list.
The user enters a name for the new grocery list.
The UI class will ask the GroceryListSystem controller class to create a grocery list.
The GroceryListSystem class communicates with the GroceryListManager to create a new grocery list, and the GroceryListSystem stores this in the EntityRepository.
This will automatically select the new grocery list.
The UI will display the current list of items, and prompt the user to go back to the list of grocery lists, add items to the grocery list, or log out.
The user chooses to add an item, and the same process as above occurs.