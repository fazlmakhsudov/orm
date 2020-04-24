package com.practice.orm.crud.repository;

import java.util.List;

public interface ICrudRepository<Class, Integer> {
	Class add(Class object);

	Class find(int id);

	boolean modify(Class object);

	boolean remove(Class object);

	List<Class> findAll();
}
