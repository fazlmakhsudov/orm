package com.practice.orm.annotation.entity.DBHandlers;

import java.lang.reflect.Field;

public class ForeignKey extends ColumnDB {
    private Class<?> clazz;


    public ForeignKey(Class<?> clazz) {
        super();
        this.clazz = clazz;
    }

    public ForeignKey(ColumnDB columnDB, Class<?> clazz) {
        super(columnDB);
        this.clazz = clazz;
    }

    public ForeignKey(Field field, String name, String type, int length, Boolean nullable, Class<?> clazz) {
        super(field, name, type, length, nullable);
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
