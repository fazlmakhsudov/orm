package com.practice.orm.crud.repository.test;

import com.practice.orm.Animal;
import com.practice.orm.Zoo;
import com.practice.orm.annotation.entity.DBHandlers.ColumnDB;
import com.practice.orm.annotation.entity.entityHandler.ColumnMarker;
import com.practice.orm.annotation.entity.entityHandler.Handler;
import com.practice.orm.annotation.entity.generatorTable.Creator;
import com.practice.orm.crud.dispetcher.OrderForCrud;
import com.practice.orm.crud.service.ICrudService;
import org.bson.io.BsonOutput;

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
//		Creator.addAnnotatedClass(Animal.class);
		Creator.addAnnotatedClass(Customer.class);
		Creator.addAnnotatedClass(Zoo.class);
		Creator.build();

		// TAble exist
//
		Zoo zoo = new Zoo(22);
//		Set<Animal> animal = new HashSet<>();
		Customer customer1 = new Customer("Federic1", "Babarian1", 22);
		Customer customer2 = new Customer("Federic2", "Babarian2", 21);
		Customer customer3 = new Customer("Federic3", "Babarian3", 23);
		Customer customer4 = new Customer("Federic4", "Babarian4", 24);

//		customer1.setAnimal(animal);
		customer1.setQwerty(zoo);







//		System.out.println(Handler.getClassByTableName(Handler.getNameTable(Zoo.class)));
//		System.out.println(Handler.isBean(Animal.class));
//		System.out.println(Handler.isBean(Zoo.class));
//		Customer customer3 = new Customer("Federic3", "Babarian3", 3);
////		Customer customer4 = new Customer("Federic4", "Babarian4", 4);
		ICrudService.create(customer1);
		OrderForCrud orderForCrud = new OrderForCrud(customer1);
		Map<Integer, List<Object>>  ord = orderForCrud.getOrderMap();
		ord.forEach((integer, objects) -> System.out.println(integer + " : " + objects));
//		ICrudService.create(customer2);
//		ICrudService.create(customer3);
//		ICrudService.create(customer4);

//		System.out.println(ICrudService.read(1, Customer.class).toString() + " :: read");
//		zoo.setId(33);
//		System.out.println(ICrudService.update(1, customer1));
//		System.out.println("delete: " + ICrudService.delete(1, Customer.class ));;
//		System.out.println(ICrudService.readAll(Customer.class));
//		Handler.getNamesTable(Handler.getClassesNamedEntity()).forEach((d,a)->{
//			System.out.println(d.getSimpleName() + "    " + a);
//		});
//		System.out.println("*******");
//		Handler.getFieldByName(Customer.class).forEach((s,d) -> System.out.println(s + "  " + d));
//		Handler.getClassesNamedEntity().stream().forEach(d-> System.out.println(d));
	}
}
