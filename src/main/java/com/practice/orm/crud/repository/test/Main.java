package com.practice.orm.crud.repository.test;

import com.practice.orm.annotation.entity.generatorTable.Creator;
import com.practice.orm.crud.service.ICrudService;

public class Main {
    public static void main(String[] args) throws Exception {
        Creator.addAnnotatedClass(Animal.class);
        Creator.addAnnotatedClass(Zoo.class);
        Creator.addAnnotatedClass(Zoo.class);
        Creator.build();



    }
}
