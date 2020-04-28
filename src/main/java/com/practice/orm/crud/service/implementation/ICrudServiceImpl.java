package com.practice.orm.crud.service.implementation;

import java.util.List;
import com.practice.orm.crud.repository.implementation.ICrudRepositoryImpl;
import com.practice.orm.crud.service.ICrudService;

public class ICrudServiceImpl<C> implements ICrudService<C, Integer> {

	private ICrudRepositoryImpl<C> crudRepository;

	public ICrudServiceImpl(ICrudRepositoryImpl crudRepository) {
		this.crudRepository = crudRepository;
	}

	@Override
	public void create(C object) {
		
	}

	@Override
	public void read(int id) {
		
	}

	@Override
	public void update(C object) {
		
	}

	@Override
	public void delete(C object) {
		
	}

	@Override
	public List<C> readAll() {
		return crudRepository.findAll();
	}

}
