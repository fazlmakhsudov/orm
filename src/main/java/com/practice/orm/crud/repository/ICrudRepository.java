package com.practice.orm.crud.repository;

import java.util.List;

public interface ICrudRepository<C, Integer> {
	C add(C object);

	C find(int id);

	boolean modify(C object);

	boolean remove(C object);

	List<C> findAll();
}
