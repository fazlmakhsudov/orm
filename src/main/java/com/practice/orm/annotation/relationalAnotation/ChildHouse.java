package com.practice.orm.annotation.relationalAnotation;

import com.practice.orm.annotation.entity.Entity;
import com.practice.orm.annotation.entity.Id;

import java.util.List;

@Entity
public class ChildHouse {
    @Id
    private Integer integer;

    @ManyToMany
    private List<Cat> cats;
}
