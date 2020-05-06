package com.practice.orm.crud.repository.test;

import java.sql.SQLException;

public class Main {
	public static void main(String[] args) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
		ICustomer iCustomer = new ICustomer();
		Customer customer = new Customer("Federic", "Babarian", 112);
		iCustomer.add(customer);
//		Customer c = iCustomer.find(1, Customer.class);
//		System.out.println(c);
		iCustomer.modify(1, new Customer("GGG","S",99));
//		System.out.println(iCustomer.findAll(Customer.class));
//		System.out.println(iCustomer.remove(1, Customer.class));
	}
}
