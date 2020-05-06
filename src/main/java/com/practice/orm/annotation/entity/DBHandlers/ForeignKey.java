package com.practice.orm.annotation.entity.DBHandlers;

import java.lang.reflect.Field;
import java.util.Objects;

public class ForeignKey extends ColumnDB {
    private Class<?> clazz;
    private String description;
    private String NameColumnTo;
    private String NameTableTo;
    private String NameTableFrom;

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNameColumnTo() {
        return NameColumnTo;
    }

    public void setNameColumnTo(String nameColumnTo) {
        NameColumnTo = nameColumnTo;
    }

    public String getNameTableTo() {
        return NameTableTo;
    }

    public void setNameTableTo(String nameTableTo) {
        NameTableTo = nameTableTo;
    }

    public ForeignKey() {
    }

    public ForeignKey(ColumnDB columnDB) {
        super(columnDB);
    }

    public ForeignKey(Field field, String name, String type, int length, Boolean nullable) {
        super(field, name, type, length, nullable);
    }

    public ForeignKey(Class<?> clazz, String description, String nameColumnTo, String nameTableTo) {
        this.clazz = clazz;
        this.description = description;
        NameColumnTo = nameColumnTo;
        NameTableTo = nameTableTo;
    }

    public ForeignKey(ColumnDB columnDB, Class<?> clazz, String description, String nameColumnTo, String nameTableTo) {
        super(columnDB);
        this.clazz = clazz;
        this.description = description;
        NameColumnTo = nameColumnTo;
        NameTableTo = nameTableTo;
    }

    public ForeignKey(Field field, String name, String type, int length, Boolean nullable, Class<?> clazz, String description, String nameColumnTo, String nameTableTo) {
        super(field, name, type, length, nullable);
        this.clazz = clazz;
        this.description = description;
        NameColumnTo = nameColumnTo;
        NameTableTo = nameTableTo;
    }

    public String getNameTableFrom() {
        return NameTableFrom;
    }

    public void setNameTableFrom(String nameTableFrom) {
        NameTableFrom = nameTableFrom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ForeignKey that = (ForeignKey) o;
        return Objects.equals(clazz, that.clazz) &&
                Objects.equals(description, that.description) &&
                Objects.equals(NameColumnTo, that.NameColumnTo) &&
                Objects.equals(NameTableTo, that.NameTableTo) &&
                Objects.equals(NameTableFrom, that.NameTableFrom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clazz, description, NameColumnTo, NameTableTo, NameTableFrom);
    }
}
