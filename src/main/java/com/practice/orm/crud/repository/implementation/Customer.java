package com.practice.orm.crud.repository.implementation;

import com.practice.orm.annotation.entity.Column;
import com.practice.orm.annotation.entity.Entity;
import com.practice.orm.annotation.entity.Id;
import com.practice.orm.annotation.entity.Table;
import com.practice.orm.annotation.generator.Generator;

@Entity
@Table(name = "Customers")
public class Customer {

	@Id
	@Column
	@Generator
	private int id;

	@Column(name = "firstname", nullable = false)
	private String firstname;

	@Column(name = "lastname", nullable = false)
	private String lastname;

	@Column(name = "age", nullable = false)
	private int age;

	public Customer(int id, String firstname, String lastname, int age) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.age = age;
	}

	public Customer(String firstname, String lastname, int age) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.age = age;
	}
}
