package com.practice.orm.crud.repository.test;

import com.practice.orm.annotation.entity.Column;
import com.practice.orm.annotation.entity.Entity;
import com.practice.orm.annotation.entity.Id;
import com.practice.orm.annotation.entity.Table;
import com.practice.orm.annotation.generator.Generator;
import com.practice.orm.annotation.relationalAnotation.ManyToOne;

@Entity
@Table(name = "qaz")
public class Animal {
    @Id
    @Generator
    private Integer id;

    @Column
    private String name;

    @Column
    private String type;

    @Column
    private double weight;

    @ManyToOne
    private Zoo zoo;

    public Animal(String name, String type, double weight) {
        this.name = name;
        this.type = type;
        this.weight = weight;
    }

    public Animal() {
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", weight=" + weight +
                ", zoo=" + zoo +
                '}';
    }
}
