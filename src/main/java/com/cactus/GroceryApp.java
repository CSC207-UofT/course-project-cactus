/*
 * Main application to run
 */
package com.cactus;

import com.cactus.adapters.AuthAdapter;
import com.cactus.adapters.ClassAuthAdapter;
import com.cactus.adapters.ClassGroceryAdapter;
import com.cactus.adapters.GroceryAdapter;
import com.cactus.data.EntityRepository;
import com.cactus.systems.GroceryListSystem;
import com.cactus.systems.UserSystem;
import com.cactus.ui.UI;

public class GroceryApp {

    public static void main(String[] args) {
        EntityRepository er = new EntityRepository();
        AuthAdapter authAdapter = new ClassAuthAdapter(er);
        GroceryAdapter groceryAdapter = new ClassGroceryAdapter(er);
        GroceryListSystem groceryListSystem = new GroceryListSystem(groceryAdapter);
        UserSystem userSystem = new UserSystem(authAdapter);

        UI ui = new UI(userSystem, groceryListSystem);
        ui.run();
    }

}
