package com.practice.orm.crud.repository.implementation;

import java.util.List;

import com.practice.orm.crud.repository.ICrudRepository;

public class ICrudRepositoryImpl implements ICrudRepository<Class, Integer> {
	
	//public ICrudRepositoryImpl(DBUtil dbUtil, QueryFormer queryFormer) {
	//}
	
	@Override
	public Class add(Class object) {
		return null;
	}

	@Override
	public Class find(int id) {
		return null;
	}

	@Override
	public boolean modify(Class object) {
		return false;
	}

	@Override
	public boolean remove(Class object) {
		return false;
	}

	@Override
	public List<Class> findAll() {
		return null;
	}

}
