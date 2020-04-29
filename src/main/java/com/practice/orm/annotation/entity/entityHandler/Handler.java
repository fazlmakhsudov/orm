package com.practice.orm.annotation.entity.entityHandler;

import com.practice.orm.annotation.entity.DBHandlers.ColumnForDB;
import com.practice.orm.annotation.entity.Entity;
import com.practice.orm.annotation.entity.Id;
import com.practice.orm.annotation.entity.Column;
import com.practice.orm.annotation.entity.Table;
import com.practice.orm.annotation.entity.DBHandlers.TableDB;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class Handler {
    private static Reflections reflections;
    private static List<Class> classes = new LinkedList<>();

    public static void addAnnotatedClass(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Entity.class)) {
            classes.add(clazz);
        }
    }

    public static Set<Class<?>> getClassesNamedEntity() {
        Set<Class<?>> setClasses = new HashSet<>();
        for (Class<?> clazz :
                classes) {
            if (clazz.isAnnotationPresent(Entity.class))
                setClasses.add(clazz);
        }
        return setClasses;
    }

    public static void SetReflection(Reflections reflections)
    {
        reflections = reflections;
    }

    public static Map<String, List<String>> getTable() {
        Map<String, List<String>> table = new HashMap<>();
        for (TableDB t : getTablesDB()) {
            List<String> columnsName = new ArrayList<>();
            columnsName.add(t.getPrimaryKey().getName());
            for (ColumnForDB c : t.getColumnForDBS()) {
                columnsName.add(c.getName());
            }
            table.put(t.getTableName(), columnsName);
        }
        return table;
    }

    public static TableDB getTableDB(Class<?> clazz) {
        TableDB tableDB = new TableDB();
        tableDB.setColumnForDBS(getColumns(clazz));
        tableDB.setTableName(getNameTable(clazz));
        tableDB.setPrimaryKey(getId(clazz));
        return tableDB;
    }

    public static Set<TableDB> getTablesDB() {
        Set<Class<?>> classes = getClassesNamedEntity();
        Set<TableDB> tableDBS = new HashSet<TableDB>();
        for (Class<?> clazz : classes) {
            tableDBS.add(getTableDB(clazz));
        }
        return tableDBS;
    }

    private static Map<Class<?>, String> getNamesTable(Set<Class<?>> classes) {
        Map<Class<?>, String> names = new HashMap<Class<?>, String>();
        for (Class<?> clazz :
                classes) {
            names.put(clazz, getNameTable(clazz));
        }
        return names;
    }

    private static Set<Field> getFieldsNamedByAnnotation(Class<?> classes,
                                                         Class<? extends Annotation> annotation) {
        Set<Field> fields = Arrays.stream(classes.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(annotation))
                .peek(field -> field.setAccessible(true))
                .collect(Collectors.toSet());
        return fields;
    }

    private static ColumnForDB getColumn(Field field) {
        ColumnForDB columnForDB = new ColumnForDB();
        if (field.isAnnotationPresent(Column.class)) {
            columnForDB.setName(field.getAnnotation(Column.class).name());
            if (field.getAnnotation(Column.class).nullable() == true)
                columnForDB.setNullable(true);
            else
                columnForDB.setNullable(false);
        }
        columnForDB.setLength(field.getAnnotation(Column.class).length());
        columnForDB.setField(field);
        columnForDB.setType(getSqlType(field));
        return columnForDB;
    }

    private static Set<ColumnForDB> getColumns(Class<?> clazz) {
        Set<ColumnForDB> columnsForDB = new HashSet<>();
        for (Field f :
                getFieldsNamedByAnnotation(clazz, Column.class)) {
            columnsForDB.add(getColumn(f));
        }
        return columnsForDB;
    }

    private static String getNameTable(Class<?> clazz) {
        String name = null;
        if (clazz.isAnnotationPresent(Table.class)) {
            name = clazz.getAnnotation(Table.class).name();
        } else {
            name = clazz.getSimpleName();
        }
        return name;
    }

    private static ColumnForDB getId(Class<?> clazz) {
        Field idField = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst().get();
        ColumnForDB columnForDB = new ColumnForDB();
        columnForDB.setField(idField);
        columnForDB.setName(idField.getName());
        columnForDB.setType(getSqlType(idField));
        columnForDB.setNullable(false);
        return columnForDB;
    }

    private static String getSqlType(Field field) {
        String javaType = field.getType().getSimpleName().toUpperCase();
        switch (javaType) {
            case "SHORT":
                return "SMALLINT";
            case "INT":
            case "INTEGER":
                return "INTEGER";
            case "LONG":
                return "BIGINT";
            case "BOOLEAN":
                return "BIT";
            case "DOUBLE":
                return "REAL";
            case "FLOAT":
                return "FLOAT";
            case "BIGDECIMAL":
                return "NUMERIC";
            case "CHARACTER":
                return "CHAR";
            case "STRING":
                return "VARCHAR";
            default:
                return null;
        }
    }

}
