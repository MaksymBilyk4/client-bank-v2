package com.clientbank.max.dao;

import java.util.List;

public interface Dao<T> {
    List<T> findAll();

    T getOne(Long id);

    T save(T obj);

    void saveAll(List<T> entities);

    void deleteAll(List<T> entities);

    boolean delete(T obj);

    boolean deleteById(Long id);
}
