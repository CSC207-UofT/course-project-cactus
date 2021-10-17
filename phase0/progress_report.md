# Progress Report

### Summary of Project

##### Specification

Our grocery management application allows users to create grocery lists either manually or on a set routine, get recommendations, and share lists with friends.

##### CRC model

Our CRC model has three entity classes (User, GroceryItem, GroceryList), two use cases (ClassAuthAdapter, ClassGroceryAdapter), two controllers(GroceryListSystem, UserSystem), and one UI (UI) as the command line interface. As the name suggests, the User, GroceryItem, GroceryList class stores all user, grocery item, and grocery list information respectively, which includes fields such as a name and an ID.

All entity classes implement an Entity interface, which specifies that each class must have some way of getting and setting and ID.

The two use case classes, ClassAuthAdapter and ClassGroceryAdapter, each implement an interface, AuthAdapter and GroceryAdapter respectively. This allows for dependency inversion, wherein the controllers need not depend on concrete classes, and instead depends on interfaces which guarantee certain behaviour. We chose this design to allow for easy modification of backend implementations in the future.

The controller directs the manager classes, and the UI directs the controller by getting user input and displaying any outputs.

##### Scenario walk-through

Our scenario walk-through consists of having a new user create a grocery list through the UI. Firstly, they create a new account, for which the information gets passed down to a use case to create an entity for User and automatically logs them in. Then, they create a new grocery list through the same process as creating a new user, add a grocery item to the list, once again using the same process, and display the list by getting the list items from a use case and having the UI print the information.

##### Skeleton program

Our skeleton program allows a user to run through the scenario from above (creating a new user, creating a new list, adding items to the list) with some additional options which have not been fully completed (empty methods with placeholders).

### Questions

Where would we store data, especially when we need to create an ID that maps to each item, and each item is an instance of an entity class?

### Design Strengths

Following CLEAN architecture principles allowed us to clearly separate our work, both in terms of dividing tasks and code functionalities. Having interfaces defined allowed for code that depended on that interface to be done concurrently with the development of concrete classes. Furthermore, following the SOLID design principles will allow our code to be easily extended and/or modified in the future. For example, our project is designed with the intent that the actual implementation of controller classes can be modified without any penalties to the UI code.

### Task Division

Caleb worked on GroceryListSystem (Receiving I/O, processing it by calling use cases, storing data, and passing data back to UI), and is planning to work further on controllers and the logic of the application in general.

Charles worked on the EntityRepository class (data storage/access), and he is planning to work further on data storage/access, and the backend of our application in general.

##### Dorsa (delete header when done)

Grace worked on the UI class and wrote up the specification and progress report. She is planning to work further on a GUI and better navigation between pages.

Ronit worked on creating the Entities, (GroceryItem, GroceryList, User). He is planning on working on a recommendation
system that suggests recurring items for the user to add to their list.