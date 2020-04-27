package com.practice.orm.crud.service;

import java.util.List;

public interface ICrudService<C, Integer> {
	C create(C object);

	C read(int id);

	boolean update(C object);
	boolean delete(C object);

	List<C> readAll();
}
