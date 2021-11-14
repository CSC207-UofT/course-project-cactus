package com.saguaro.repository;

import com.saguaro.entity.GroceryItem;
import com.saguaro.entity.GroceryList;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public class CustomGroceryListRepositoryImpl implements CustomGroceryListRepository<GroceryList> {

    @Autowired
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
