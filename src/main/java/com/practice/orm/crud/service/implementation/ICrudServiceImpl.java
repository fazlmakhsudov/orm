package com.practice.orm.crud.service.implementation;

import java.util.List;
import com.practice.orm.crud.repository.implementation.ICrudRepositoryImpl;
import com.practice.orm.crud.service.ICrudService;

public class ICrudServiceImpl implements ICrudService {

	public ICrudServiceImpl(ICrudRepositoryImpl cr) {
	}

	@Override
	public Object create(Object object) {
		return null;
	}

	@Override
	public Object read(int id) {
		return null;
	}

	@Override
	public boolean update(Object object) {
		return false;
	}

	@Override
	public boolean delete(Object object) {
		return false;
	}

	@Override
	public List readAll() {
		return null;
	}

}
