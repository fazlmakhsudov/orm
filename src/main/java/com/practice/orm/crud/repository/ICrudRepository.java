package com.practice.orm.crud.repository;

import java.sql.SQLException;
import java.util.List;

public interface ICrudRepository<C, Integer> {
    boolean add(C object);

    C find(Object id, Class clazz);

    boolean modify(Object id, C object);

    boolean remove(Object id, Class clazz) throws SQLException;

    List<C> findAll(Class clazz) throws SQLException, IllegalAccessException, InstantiationException, NoSuchFieldException;
}
