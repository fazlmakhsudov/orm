package com.practice.orm.annotation.relationalAnotation;

import com.practice.orm.annotation.entity.Column;
import com.practice.orm.annotation.entity.Entity;
import com.practice.orm.annotation.entity.Id;

import java.util.Set;

@Entity
public class Cat {
    @Id
    private Integer id;

    @Column
    private String klikuha;

    @ManyToOne
    private Set<User> users;
}
