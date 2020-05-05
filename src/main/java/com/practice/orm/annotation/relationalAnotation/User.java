package com.practice.orm.annotation.relationalAnotation;

import com.practice.orm.annotation.entity.Column;
import com.practice.orm.annotation.entity.Entity;
import com.practice.orm.annotation.entity.Id;
import com.practice.orm.annotation.entity.Table;

import java.util.List;

@Entity
@Table(name = "usr")
public class User {
    @Id
    private Integer id;

    @Column
    private String name;

    @ManyToOne
    private List<Book> books;

}
