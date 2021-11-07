package com.saguaro.repository;

import com.saguaro.entity.GroceryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroceryItemRepository extends JpaRepository<GroceryItem, Long> {

    GroceryItem findGroceryItemByName(String name);
}