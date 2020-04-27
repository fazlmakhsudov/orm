package com.practice.orm.crud.repository.implementation;

import java.util.List;
import java.sql.DriverManager;
import com.practice.orm.crud.repository.ICrudRepository;

public class ICrudRepositoryImpl<C> implements ICrudRepository<C, Integer> {

	public ICrudRepositoryImpl(DBUtil dbUtil, QueryFormer queryFormer) {
		
	}

	@Override
	public C add(C object) {
		
		return null;
	}

	@Override
	public C find(int id) {
		
		return null;
	}

	@Override
	public boolean modify(C object) {
		
		return false;
	}

	@Override
	public boolean remove(C object) {
		
		return false;
	}

	@Override
	public List<C> findAll() {
		
		return null;
	}

}
