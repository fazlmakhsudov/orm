package com.practice.orm.crud.service;

import java.util.List;

import com.practice.orm.crud.repository.implementation.ICrudRepositoryImpl;
import com.practice.orm.db.utilDao.entiry.DBUtil;
import com.practice.orm.db.utilDao.entiry.QueryFormer;

public interface ICrudService {
	// ICrudRepositoryImpl iCrudRepository = new
	// ICrudRepositoryImpl(DBUtil.getInstance(),
	// QueryFormer.getTableQueries());

	static <C> void create(C object) {

	}

	static void read(int id) {

	}

	static <C> void update(C object) {

	}

	static <C> void delete(C object) {

	}

	static <C> List<C> readAll() {
		return null;
	}

}
