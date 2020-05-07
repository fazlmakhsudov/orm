package com.practice.orm.crud.repository.test;

import com.practice.orm.crud.service.ICrudService;

import java.sql.SQLException;

public class Main {
	public static void main(String[] args) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
		//Dima creates tables

		// TAble exist
		Customer customer1 = new Customer("Federic1", "Babarian1", 1);
		Customer customer2 = new Customer("Federic2", "Babarian2", 2);
		Customer customer3 = new Customer("Federic3", "Babarian3", 3);
		Customer customer4 = new Customer("Federic4", "Babarian4", 4);

		ICrudService.create(customer1);
		ICrudService.create(customer2);
		ICrudService.create(customer3);
		ICrudService.create(customer4);

		System.out.println(ICrudService.read(3, Customer.class).toString() + " :: read");
		System.out.println(ICrudService.update(1, new Customer("update", "surnamupta", 33)));
		System.out.println("delete: " + ICrudService.delete(2, Customer.class ));;
		System.out.println(ICrudService.readAll(Customer.class));


	}
}
