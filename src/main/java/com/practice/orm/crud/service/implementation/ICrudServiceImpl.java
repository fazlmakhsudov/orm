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
	public C create(C object) {
		return crudRepository.add(object);
	}

	@Override
	public C read(int id) {
		return crudRepository.find(id);
	}

	@Override
	public boolean update(C object) {
		return crudRepository.modify(object);
	}

	@Override
	public boolean delete(C object) {
		return crudRepository.remove(object);
	}

	@Override
	public List<C> readAll() {
		return crudRepository.findAll();
	}

}
