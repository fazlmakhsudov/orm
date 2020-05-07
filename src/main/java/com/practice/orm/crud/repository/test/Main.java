package com.practice.orm.crud.repository.test;

import com.practice.orm.Animal;
import com.practice.orm.Zoo;
import com.practice.orm.annotation.entity.DBHandlers.ColumnDB;
import com.practice.orm.annotation.entity.entityHandler.ColumnMarker;
import com.practice.orm.annotation.entity.entityHandler.Handler;
import com.practice.orm.annotation.entity.generatorTable.Creator;
import com.practice.orm.crud.service.ICrudService;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
	public static void main(String[] args) throws Exception {
		//Dima creates tables
//		Creator.addAnnotatedClass(Zoo.class);
		Creator.addAnnotatedClass(Animal.class);
		Creator.addAnnotatedClass(Customer.class);
		Creator.addAnnotatedClass(Zoo.class);
		Creator.build();

		// TAble exist

		Zoo zoo = new Zoo();
		Set<Animal> animal = new HashSet<>();
		Customer customer1 = new Customer("Federic2", "Babarian2", 2);
		customer1.setAnimal(animal);
		customer1.setQwerty(zoo);
		System.out.println(Handler.getClassByTableName(Handler.getNameTable(Zoo.class)));
		System.out.println(Handler.isBean(Animal.class));
		System.out.println(Handler.isBean(Zoo.class));
//		Customer customer3 = new Customer("Federic3", "Babarian3", 3);
//		Customer customer4 = new Customer("Federic4", "Babarian4", 4);
		ICrudService.create(customer1);
//		ICrudService.create(customer2);
//		ICrudService.create(customer3);
//		ICrudService.create(customer4);
//
//		System.out.println(ICrudService.read(3, Customer.class).toString() + " :: read");
//		System.out.println(ICrudService.update(1, new Customer("update", "surnamupta", 33)));
//		System.out.println("delete: " + ICrudService.delete(2, Customer.class ));;
//		System.out.println(ICrudService.readAll(Customer.class));


	}
}
