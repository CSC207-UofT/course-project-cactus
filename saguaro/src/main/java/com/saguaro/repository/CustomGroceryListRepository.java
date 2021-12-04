package com.saguaro.repository;

/**
 * A repository interface used to override the save method. A parameter T must be used
 * to resolve ambiguity errors. For the purposes of Saguaro, this parameter will always
 * be GroceryList.
 *
 * @param <T> set to GroceryList when using this interface to override the save method for GroceryLists
 * @author Charles Wong
 */
interface CustomGroceryListRepository<T> { // need the T to resolve ambiguity

    /**
     * Saves an entity, and returns the saved instance.
     *
     * @param entity the entity to save
     * @param <S>    the type of the entity to save
     * @return the saved entity instance
     */
    <S extends T> S save(S entity);
}
