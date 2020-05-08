package com.practice.orm.crud.service;

import com.practice.orm.crud.repository.ICrudRepository;
import com.practice.orm.crud.repository.implementation.ICrudRepositoryImpl;

import java.sql.SQLException;
import java.util.List;


public interface ICrudService<C> {


    static <C> boolean create(C object) {
        ICrudRepository<C, Integer> iCrudRepository = new ICrudRepositoryImpl<>();
        return iCrudRepository.add(object);
    }

    static <C> C read(Object id, Class cl) {
        ICrudRepository<C, Integer> iCrudRepository = new ICrudRepositoryImpl<>();
        return iCrudRepository.find(id, cl);
    }

    static <C> boolean update(Object id, C object) {
        ICrudRepository<C, Integer> iCrudRepository = new ICrudRepositoryImpl<>();
        return iCrudRepository.modify(id, object);
    }

    static <C> boolean delete(Object id, Class cl) {
        ICrudRepository<C, Integer> iCrudRepository = new ICrudRepositoryImpl<>();
        boolean flag = false;
        try {
            flag = iCrudRepository.remove(id, cl);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    static <C> List<C> readAll(Class cl) {
        ICrudRepository<C, Integer> iCrudRepository = new ICrudRepositoryImpl<>();
        List<C> objects = null;
        try {
            objects = iCrudRepository.findAll(cl);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return objects;
    }

}
