package com.practice.orm.annotation.entity.generatorTable;

import com.practice.orm.annotation.entity.DBHandlers.TableDB;

import java.util.Set;

final public class GeneratorTable {

    public static void generate(Set<TableDB> tableDBS) {
        try {
            GenerateHandler.dropTables(tableDBS);
            GenerateHandler.generateTables(tableDBS);
            GenerateHandler.generateForeignKeys(tableDBS);
            GenerateHandler.generateRelation(tableDBS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
