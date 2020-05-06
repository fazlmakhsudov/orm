package com.practice.orm.crud.repository.test;

import com.practice.orm.annotation.entity.entityHandler.Handler;
import com.practice.orm.annotation.entity.generatorTable.Creator;
import com.practice.orm.annotation.generator.GeneratorHandler;
import com.practice.orm.crud.service.ICrudService;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        Creator.addAnnotatedClass(Animal.class);
        Creator.addAnnotatedClass(Zoo.class);
//        1)List<Class> classes = Handler.getClasses();
//        or
//        1)Set<Class<?>> classesNamedEntity = Handler.getClassesNamedEntity();

//        2)Map<String, List<String>> table = Handler.getTable();
//        System.out.println(table.get(Handler.getNameTable(Zoo.class)));

//        3)Map<Class<?>, String> namesTable = Handler.getNamesTable(Handler.getClassesNamedEntity());

//        4)Map<String, Field> fieldByName = Handler.getFieldByName(Zoo.class);

        Creator.build();
        // Crud
//       Animal an = new Animal("Pe", "We", 22);
//       ICrudService.create(an);

    }
}
