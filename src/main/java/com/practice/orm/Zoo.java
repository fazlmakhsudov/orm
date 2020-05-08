package com.practice.orm;

import com.practice.orm.annotation.entity.Column;
import com.practice.orm.annotation.entity.Entity;
import com.practice.orm.annotation.entity.Id;
import com.practice.orm.annotation.entity.Table;

@Entity
@Table(name = "ZooPark")
public class Zoo {
    @Id
    private int id;

    @Column
    private String name;

    @Column
    private String street;

    @Column
    private float price;

    public Zoo(int id) {
        this.id = id;
    }
    public Zoo() {
    }
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Zoo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", street='" + street + '\'' +
                ", price=" + price +
                '}';
    }
}
