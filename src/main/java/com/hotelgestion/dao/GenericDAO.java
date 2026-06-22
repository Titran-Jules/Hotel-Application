package com.hotelgestion.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T, ID> {
    T create(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    boolean update(T entity);
    boolean delete(ID id);
}
