package com.practice.orm.crud.service;

import java.util.List;

import com.practice.orm.crud.repository.implementation.ICrudRepositoryImpl;
import com.practice.orm.db.utilDao.entiry.DBUtil;
import com.practice.orm.db.utilDao.entiry.QueryFormer;

public interface ICrudService<C, Integer> {
	//ICrudRepositoryImpl iCrudRepository = new ICrudRepositoryImpl(DBUtil.getInstance(),
	//		QueryFormer.getTableQueries());

	void create(C object);

	void read(int id);

	void update(C object);

	void delete(C object);

	List<C> readAll();

}
