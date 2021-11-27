package com.saguaro.repository;

import com.saguaro.entity.GroceryItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface defining grocery item database interaction methods. Methods defined
 * here are automatically generated implementations by Spring through reflection magic.
 * Extending JpaRepository also provides some default repository methods.
 *
 * @author Charles Wong
 */
public interface GroceryItemRepository extends JpaRepository<GroceryItem, Long> {

    /**
     * Find a grocery item given its name.
     *
     * @param name the String name of the GroceryItem to find
     * @return the GroceryItem with the given name, or null if it does not exist
     */
    GroceryItem findGroceryItemByName(String name);
}