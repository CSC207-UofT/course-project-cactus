package com.saguaro.repository;

interface CustomGroceryListRepository <T> { // need the T to resolve ambiguity

    <S extends T> S save(S entity);
}
