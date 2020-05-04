package com.practice.orm.annotation.relationalAnotation;

import com.practice.orm.annotation.entity.Column;
import com.practice.orm.annotation.entity.Entity;
import com.practice.orm.annotation.entity.Id;

import java.util.List;

@Entity
public class Book {
    @Id
    private Integer id;

    @Column(name = "genre")
    private String genre;

    @ManyToMany(
            nameTable = "dima",
            joinColumn = @JoinColumn(name = "q"),
            inverseJoinColumn = @JoinColumn(name = "q"))
    private List<User> users;
}
