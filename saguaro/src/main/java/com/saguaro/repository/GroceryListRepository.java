package com.saguaro.repository;

import com.saguaro.entity.GroceryList;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface defining grocery list database interaction methods. Methods defined
 * here are automatically generated implementations by Spring through reflection magic.
 * Extending JpaRepository also provides some default repository methods.
 *
 * @author Charles Wong
 */
public interface GroceryListRepository extends JpaRepository<GroceryList, Long>, CustomGroceryListRepository<GroceryList> {

    /**
     * Find a grocery list given its ID.
     *
     * @param id a long representing the ID of the GroceryList to search for
     * @return the GroceryList with the given ID, or null if it does not exist
     */
    GroceryList findGroceryListById(long id);
}
