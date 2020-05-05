package com.practice.orm.crud.service;

import java.util.List;



public interface ICrudService<C> {

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
