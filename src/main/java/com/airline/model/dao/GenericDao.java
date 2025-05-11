// GenericDao.java
package com.airline.model.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, K> {
    Optional<T> findById(K id);
    List<T> findAll();
    T save(T entity);
    boolean update(T entity);
    boolean delete(K id);
}