package com.practice.orm.crud.repository.test;

import com.practice.orm.annotation.entity.Column;
import com.practice.orm.annotation.entity.Entity;
import com.practice.orm.annotation.entity.Id;
import com.practice.orm.annotation.relationalAnotation.ManyToOne;

@Entity
public class Animal {
    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private String type;

    @Column
    private double weight;

    @ManyToOne
    private Zoo zoo;
}
