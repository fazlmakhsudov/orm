package com.practice.orm.crud.repository.test;

public class Main {
	public static void main(String[] args) {
		ICustomer iCustomer = new ICustomer();
		Customer customer = new Customer("P", "V", 12);
		iCustomer.add(customer);
		iCustomer.find(3, Customer.class);
		iCustomer.modify(3, new Customer("G","S",99));
	}
}
