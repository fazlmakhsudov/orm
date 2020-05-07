package com.practice.orm;

import com.practice.orm.annotation.entity.generatorTable.Creator;

public class Main {
    public static void main(String[] args) throws Exception {
        Creator.addAnnotatedClass(Animal.class);
        Creator.addAnnotatedClass(Zoo.class);
        Creator.build();
//         1) Dispatcher CRUD
//           -SCAN OBJECT -> Map<ZOO_Id,List<ZOO-Object,ANIMAL-OBJeCT>
//           OrderList(ZOO-OBJECT) -> Map<Object,ParentID>

//        1)List<Class> classes = Handler.getClasses();
//        or
//        1)Set<Class<?>> classesNamedEntity = Handler.getClassesNamedEntity();

//        2)Map<String, List<String>> table = Handler.getTable();
//        System.out.println(table.get(Handler.getNameTable(Zoo.class)));

//        3)Map<Class<?>, String> namesTable = Handler.getNamesTable(Handler.getClassesNamedEntity());

//        4)Map<String, Field> fieldByName = Handler.getFieldByName(Zoo.class);

       
        // Crud
//       Animal an = new Animal("Pe", "We", 22);
//       ICrudService.create(an);

    }
}
