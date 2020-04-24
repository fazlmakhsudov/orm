package com.practice.orm.crud.service;

import java.util.List;

public interface ICrudService<Class, Integer> {
	Class create(Class object);

	Class read(int id);

	boolean update(Class object);

	boolean delete(Class object);

	List<Class> readAll();
}
