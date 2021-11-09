package com.saguaro.repository;

import com.saguaro.entity.GroceryList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroceryListRepository extends JpaRepository<GroceryList, Long>, CustomGroceryListRepository<GroceryList> {

    GroceryList findGroceryListById(long id);
}
