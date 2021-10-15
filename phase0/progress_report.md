# Progress Report

### Summary of Project

##### Specification

Our grocery management application allows users to create grocery lists either manually or on a set routine, get recommendations, and share lists with friends.

##### CRC model

Our CRC model has three entity classes (User, GroceryItem, GroceryList), two use cases (UserManager, GroceryListManager), one controller (GroceryListSystem), and one UI (UI) as the command line interface. As the name suggests, the User, GroceryItem, GroceryList class stores all user, grocery item, and grocery list information respectively, which includes a name and an ID. The Manager classes interact with the database to add and remove users and grocery lists/items. The controller directs the manager classes, and the UI directs the controller by getting the user input and displaying any outputs.

##### Scenario walk-through

Our scenario walk-through consists of having a new user create a grocery list through the UI. Firstly, they create a new account, for which the information gets passed down to a use case to create an entity for User and automatically logs them in. Then, they create a new grocery list through the same process as creating a new user, add a grocery item to the list, once again using the same process, and display the list by getting the list items from a use case and having the UI print the information.

##### Skeleton program

Our skeleton program allows a user to run through the scenario from above (creating a new user, creating a new list, adding items to the list) with some additional options which have not been fully completed (empty methods with placeholders).

### Questions

Where would we store data, especially when we need to create an ID that maps to each item, and each item is an instance of an entity class?

### Design Strengths

Following clean architecture allowed us to clearly separate our work, both in terms of dividing tasks and code functionalities. For example, by having our UI class only interact with the controller class (GroceryListSystem), we make our code easier to understand on a high level when reading through the UI class.

### Task Division

Caleb worked on GroceryListSystem (Receiving I/O, processing it by calling use cases, storing data, and passing data back to UI), and is planning to work further on controllers and the logic of the application in general.

Charles worked on EntityRepository (data storage/access), and he is planning to work further on data storage/access, and the backend of our application in general.

##### Dorsa (delete header when done)

Grace worked on the UI class and wrote up the specification and progress report. She is planning to work further on a GUI and better navigation between pages.

##### Ronit (delete header when done)

Working on: Entities
Planning to work on: