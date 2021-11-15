package com.saguaro.repository;

import com.saguaro.entity.GroceryItem;
import com.saguaro.entity.GroceryList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CustomGroceryListRepositoryImpl implements CustomGroceryListRepository<GroceryList> {

    @PersistenceContext
    EntityManager entityManager;

    /**
     *
     * @param entity the GroceryList to be saved
     * @param <S> any subclass of GroceryList
     * @return the
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
