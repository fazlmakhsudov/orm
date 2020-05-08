package com.practice.orm.crud.repository.test;

import com.practice.orm.Animal;
import com.practice.orm.Zoo;
import com.practice.orm.annotation.entity.Column;
import com.practice.orm.annotation.entity.Entity;
import com.practice.orm.annotation.entity.Id;
import com.practice.orm.annotation.entity.Table;
import com.practice.orm.annotation.entity.entityHandler.ColumnMarker;
import com.practice.orm.annotation.generator.Generator;
import com.practice.orm.annotation.relationalAnotation.ManyToMany;
import com.practice.orm.annotation.relationalAnotation.ManyToOne;
import com.practice.orm.annotation.relationalAnotation.OneToMany;

import java.util.Set;

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

//	@ManyToOne
//	@ColumnMarker
	private Zoo qwerty;
//
//	@ManyToMany
//	@ColumnMarker
	private Set<Animal> animal;

	public Set<Animal> getAnimal() {
		return animal;
	}

	public void setAnimal(Set<Animal> animal) {
		this.animal = animal;
	}

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

	public Customer() {
	}

	public Zoo getQwerty() {
		return qwerty;
	}

	public void setQwerty(Zoo qwerty) {
		this.qwerty = qwerty;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", age=" + age + "]";
	}

}
