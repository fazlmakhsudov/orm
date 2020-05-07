package com.practice.orm.annotation.entity;

import com.practice.orm.annotation.entity.DBHandlers.TableDB;
import com.practice.orm.annotation.entity.entityHandler.Handler;
import com.practice.orm.annotation.entity.generatorTable.Creator;

import java.util.Set;

public class Main {
    public static void main(String[] args) {
        try {
        Creator.addAnnotatedClass(User.class);
        Creator.addAnnotatedClass(Cat.class);
        Creator.addAnnotatedClass(Book.class);
        Creator.addAnnotatedClass(ChildHouse.class);
        Set<TableDB> tablesDB = Handler.getTablesDB();
        for (TableDB table :
                tablesDB) {
            System.out.println(table.getTableName());
        }
        System.out.println(tablesDB.size());

        Creator.build();

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
