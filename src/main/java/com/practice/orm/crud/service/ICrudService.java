package com.practice.orm.crud.service;

import java.util.List;

import com.practice.orm.crud.repository.implementation.ICrudRepositoryImpl;

public interface ICrudService<C, Integer> {
	//ICrudRepositoryImpl<C> iCrudRepository = new ICrudRepositoryImpl<C>(DBUtil.getInstance(),
	//QueryFormer.getTableQueries());

	C create(C object);

	C read(int id);

	boolean update(C object);

	boolean delete(C object);

	List<C> readAll();

	// дефолтные методы где создавать объект
}
