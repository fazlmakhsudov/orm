package com.practice.orm.annotation.relationalAnotation;

import com.practice.orm.annotation.entity.Column;
import com.practice.orm.annotation.entity.Entity;
import com.practice.orm.annotation.entity.Id;
import com.practice.orm.annotation.entity.Table;

import java.util.List;

@Entity
@Table(name = "kniga")
public class Book {
    @Id
    private Integer id;

    @Column(name = "genre")
    private String genre;

//    @OneToMany
//    private List<User> users;






}
