package com.practice.orm.annotation.entity.DBHandlers;

import java.lang.reflect.Field;
import java.util.Objects;

public class ColumnDB {

    private Field field;
    private String name;
    private String type;
    private int length;
    private Boolean nullable;

    public ColumnDB() {
    }

    public ColumnDB(ColumnDB columnDB) {
        this.field = columnDB.field;
        this.name = columnDB.name;
        this.type = columnDB.type;

    }

    public ColumnDB(Field field, String name, String type, int length, Boolean nullable) {
        this.field = field;
        this.name = name;
        this.type = type;
        this.length = length;
        this.nullable = nullable;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColumnDB columnDB = (ColumnDB) o;
        return length == columnDB.length &&
                Objects.equals(field, columnDB.field) &&
                Objects.equals(name, columnDB.name) &&
                Objects.equals(type, columnDB.type) &&
                Objects.equals(nullable, columnDB.nullable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, name, type, length, nullable);
    }
}
