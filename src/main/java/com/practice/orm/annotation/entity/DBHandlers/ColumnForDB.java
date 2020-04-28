package com.practice.orm.annotation.entity.DBHandlers;

import java.lang.reflect.Field;

public class ColumnForDB {
    private String name;
    private String type;
    private Field field;
    private Boolean nullable;

    public Boolean getNullable() {
        return nullable;
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

}
