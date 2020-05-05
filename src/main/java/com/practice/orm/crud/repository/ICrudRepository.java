package com.practice.orm.crud.repository;

import java.util.List;

public interface ICrudRepository<C, Integer> {
	C add(C object);

	C find(int id, Class clazz);

	boolean modify(int id, Class clazz);

	boolean remove(C object);

	List<C> findAll();
}
