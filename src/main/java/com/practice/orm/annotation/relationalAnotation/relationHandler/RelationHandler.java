package com.practice.orm.annotation.relationalAnotation.relationHandler;

import com.practice.orm.annotation.entity.DBHandlers.ColumnDB;
import com.practice.orm.annotation.entity.DBHandlers.ForeignKey;
import com.practice.orm.annotation.entity.DBHandlers.TableDB;
import com.practice.orm.annotation.entity.entityHandler.Handler;
import com.practice.orm.annotation.relationalAnotation.JoinColumn;
import com.practice.orm.annotation.relationalAnotation.ManyToMany;
import com.practice.orm.annotation.relationalAnotation.ManyToOne;
import com.practice.orm.annotation.relationalAnotation.OneToMany;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class RelationHandler {

    public static Set<ForeignKey> getForeignKey(Class<?> clazz) throws Exception {
        Field[] declaredFields = clazz.getDeclaredFields();
        Set<ForeignKey> foreignKey = new HashSet<>();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(ManyToOne.class)) {
                foreignKey.add(getForeignKeyForManyToOne(field, clazz));
            }
            if (field.isAnnotationPresent(ManyToMany.class)) {
                Handler.addRelationTable(getForeignKeyForManyToMany(field, clazz));
            }
            if (field.isAnnotationPresent(OneToMany.class)) {
                foreignKey.add(getForeignKeyForOneToMany(field,clazz));
            }

        }
        return foreignKey;
    }
    public static ForeignKey getForeignKeyForOneToMany(Field field,Class<?> clazz) throws Exception {
        ForeignKey foreignKey = new ForeignKey();
        foreignKey.setClazz(Handler.getTypeClass(field));
        if (field.getAnnotation(OneToMany.class).mappedBy().length() == 0) {
            if (field.isAnnotationPresent(JoinColumn.class)) {
                if (field.getAnnotation(JoinColumn.class).name().length() == 0) {
                    foreignKey.setName(Handler.getId(clazz).getName());
                } else {
                    foreignKey.setName(field.getAnnotation(JoinColumn.class).name());
                }
            } else {
                foreignKey.setName(Handler.getId(clazz).getName());
            }
        } else {
            foreignKey.setName(field.getAnnotation(OneToMany.class).mappedBy());
        }
        foreignKey.setType(Handler.getId(clazz).getType());
        foreignKey.setDescription(field.getType().getSimpleName());
        foreignKey.setField(field);
        foreignKey.setNameColumnTo(Handler.getId(clazz).getName());
        foreignKey.setNameTableTo(Handler.getNameTable(clazz));
        foreignKey.setNameTableFrom(Handler.getNameTable(foreignKey.getClazz()));
        foreignKey.setNullable(false);
        return foreignKey;
    }

    private static TableDB getForeignKeyForManyToMany(Field field, Class<?> clazz) throws Exception {
        TableDB tableDB = new TableDB();
        Class genericType = Handler.getTypeClass(field);
        if (field.isAnnotationPresent(ManyToMany.class)) {
            ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
            if (manyToMany.nameTable().length() == 0) {
                tableDB.setTableName(clazz.getSimpleName() + "_" + genericType.getSimpleName());
            } else
                tableDB.setTableName(field.getAnnotation(ManyToMany.class).nameTable());
            Set<ForeignKey> foreignKeys = new HashSet<>();
            if (manyToMany.joinColumn().name().length() == 0) {
                foreignKeys.add(createForeignKey(clazz, "",tableDB.getTableName()));
            } else {
                foreignKeys.add(createForeignKey(clazz, manyToMany.joinColumn().name(),tableDB.getTableName()));
            }
            if (manyToMany.inverseJoinColumn().name().length() == 0) {
                foreignKeys.add(createForeignKey(genericType, "",tableDB.getTableName()));
            } else {
                foreignKeys.add(createForeignKey(genericType, manyToMany.inverseJoinColumn().name(),tableDB.getTableName()));
            }
            if (manyToMany.primaryKey().length() == 0) {
                tableDB.setPrimaryKey(Handler.setNamePrimaryKey(clazz, tableDB.getTableName()));
            } else {
                tableDB.setPrimaryKey(Handler.setNamePrimaryKey(clazz, manyToMany.primaryKey()));
            }
            tableDB.setForeignKey(foreignKeys);
        }
        return tableDB;
    }

    private static ForeignKey getForeignKeyForManyToOne(Field field, Class<?> clazz) throws Exception {
        ForeignKey foreignKey = new ForeignKey();
        foreignKey.setClazz(Handler.getTypeClass(field));
        if (field.getAnnotation(ManyToOne.class).mappedBy().length() == 0) {
            if (field.isAnnotationPresent(JoinColumn.class)) {
                if (field.getAnnotation(JoinColumn.class).name().length() == 0) {
                    foreignKey.setName(Handler.getId(foreignKey.getClazz()).getName());
                } else {
                    foreignKey.setName(field.getAnnotation(JoinColumn.class).name());
                }
            } else {
                foreignKey.setName(Handler.getId(foreignKey.getClazz()).getName());
            }
        } else {
            foreignKey.setName(field.getAnnotation(ManyToOne.class).mappedBy());
        }
        foreignKey.setNameTableTo(Handler.getNameTable(foreignKey.getClazz()));
        foreignKey.setType(Handler.getId(foreignKey.getClazz()).getType());
        foreignKey.setDescription(field.getType().getSimpleName());
        foreignKey.setNameColumnTo(Handler.getId(foreignKey.getClazz()).getName());
        foreignKey.setField(field);
        foreignKey.setNameTableFrom(Handler.getNameTable(clazz));
        foreignKey.setNullable(false);
        return foreignKey;
    }

    private static ForeignKey createForeignKey(Class<?> clazz, String name,String nameTable) throws Exception {
        ColumnDB columnDB = Handler.getId(clazz);
        ForeignKey foreignKey = new ForeignKey();
        foreignKey.setField(columnDB.getField());
        foreignKey.setType(columnDB.getType());
        foreignKey.setNullable(columnDB.getNullable());
        foreignKey.setLength(columnDB.getLength());
        foreignKey.setClazz(clazz);
        if (name.isEmpty()) {
            foreignKey.setName(columnDB.getName());
        } else {
            foreignKey.setName(name);
        }
        foreignKey.setNameTableTo(Handler.getNameTable(clazz));
        foreignKey.setNameColumnTo(columnDB.getName());
        foreignKey.setNameTableFrom(nameTable);
        return foreignKey;
    }
}
