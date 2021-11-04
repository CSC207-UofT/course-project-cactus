package com.saguaro.repository;

import com.saguaro.entity.GroceryList;

interface CustomGroceryListRepository <T> { // need the T to resolve ambiguity

    <S extends T> S save(S entity);
}
