package com.saguaro.repository;

import com.saguaro.entity.GroceryItem;
import com.saguaro.entity.GroceryList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * A custom grocery list repository implementation, only used to override the save method. This
 * class is automatically picked up by Spring as the desired implementation of
 * CustomGroceryListRepository.
 *
 * @author Charles Wong
 */
public class CustomGroceryListRepositoryImpl implements CustomGroceryListRepository<GroceryList> {

    /**
     * An EntityManager used to interact with the persistent context
     */
    @PersistenceContext
    EntityManager entityManager;

    /**
     * Save the given GroceryList (or subclass) entity and return the saved instance. Since un-managed
     * entities cannot be persisted as children of an entity (without cascading, which is undesirable
     * in this context), this method first makes sure all child GroceryItems are persisted, and then
     * persists the GroceryList.
     *
     * @param entity the GroceryList to be saved
     * @param <S>    any subclass of GroceryList
     * @return the saved entity instance
     */
    @Override
    public <S extends GroceryList> S save(S entity) {
        for (GroceryItem item : entity.getItems()) {
            if (!entityManager.contains(item)) {
                entityManager.persist(item);
            }
        }
        entityManager.flush();

        entityManager.persist(entity); // TODO: handle exceptions better

        return entity;
    }
}
