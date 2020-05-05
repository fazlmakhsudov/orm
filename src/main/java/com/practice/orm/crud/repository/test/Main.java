package com.practice.orm.crud.repository.test;

public class Main {
    public static void main(String[] args) {
        ICustomer iCustomer = new ICustomer();
        Customer customer = new Customer("P", "V", 12);
        iCustomer.add(customer);
    }
}
