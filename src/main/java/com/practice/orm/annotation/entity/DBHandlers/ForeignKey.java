package com.practice.orm.annotation.entity.DBHandlers;

import java.lang.reflect.Field;

public class ForeignKey extends ColumnDB {
    private TableDB tableDB;


    public ForeignKey(TableDB tableDB) {
        this.tableDB = tableDB;
    }

    public ForeignKey(ColumnDB columnDB, TableDB tableDB) {
        super(columnDB);
        this.tableDB = tableDB;
    }

    public ForeignKey(Field field, String name, String type, int length, Boolean nullable, TableDB tableDB) {
        super(field, name, type, length, nullable);
        this.tableDB = tableDB;
    }
}
