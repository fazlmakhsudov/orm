package com.practice.orm.annotation.relationalAnotation;

import com.practice.orm.annotation.entity.Column;
import com.practice.orm.annotation.entity.Entity;
import com.practice.orm.annotation.entity.Id;

import java.util.List;

@Entity
public class Cat {
    @Id
    private Integer id;

    @Column(name = "klikuha")
    private String name;

    @ManyToOne
    private List<Book> books;


}
