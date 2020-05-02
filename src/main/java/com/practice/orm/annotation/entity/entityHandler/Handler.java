package com.practice.orm.annotation.entity.entityHandler;

import com.practice.orm.annotation.entity.Column;
import com.practice.orm.annotation.entity.DBHandlers.ColumnDB;
import com.practice.orm.annotation.entity.DBHandlers.TableDB;
import com.practice.orm.annotation.entity.Id;
import com.practice.orm.annotation.entity.Table;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class Handler {

    private static final List<Class> classes = new LinkedList<>();

    public static void addClass(Class<?> clazz) {
        classes.add(clazz);
    }

    public static List<Class> getClasses() {
        return classes;
    }

    public static Set<Class<?>> getClassesNamedEntity() {
        Set<Class<?>> setClasses = new HashSet<>();
        for (Class<?> clazz :
                classes) {
            setClasses.add(clazz);
        }
        return setClasses;
    }


    public static Map<String, List<String>> getTable() {
        Map<String, List<String>> table = new HashMap<>();
        for (TableDB t : getTablesDB()) {
            List<String> columnsName = new ArrayList<>();
            columnsName.add(t.getPrimaryKey().getName());
            for (ColumnDB c : t.getColumnDBS()) {
                columnsName.add(c.getName());
            }
            table.put(t.getTableName(), columnsName);
        }
        return table;
    }

    public static TableDB getTableDB(Class<?> clazz) {
        TableDB tableDB = new TableDB();
        tableDB.setColumnDBS(getColumns(clazz));
        tableDB.setTableName(getNameTable(clazz));
        try {
            tableDB.setPrimaryKey(getId(clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static Map<Class<?>, String> getNamesTable(Set<Class<?>> classes) {
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

    private static ColumnDB getColumn(Field field) {
        ColumnDB columnDB = new ColumnDB();
        if (field.isAnnotationPresent(Column.class)) {
            if (field.getAnnotation(Column.class).nullable() == true)
                columnDB.setNullable(true);
            else
                columnDB.setNullable(false);
            if (field.getAnnotation(Column.class).name() == "")
            {
                columnDB.setName(field.getAnnotation(Column.class).name());
            }
            else
            {
                columnDB.setName(field.getName());
            }
        }
        columnDB.setLength(field.getAnnotation(Column.class).length());
        columnDB.setField(field);
        columnDB.setType(getSqlType(field));
        return columnDB;
    }

    private static Set<ColumnDB> getColumns(Class<?> clazz) {
        Set<ColumnDB> columnsForDB = new HashSet<>();
        for (Field f :
                getFieldsNamedByAnnotation(clazz, Column.class)) {
            if (f.isAnnotationPresent(Id.class))
            {
                continue;
            }
            else {
                columnsForDB.add(getColumn(f));
            }
        }
        return columnsForDB;
    }

    public static String getNameTable(Class<?> clazz) {
        String name = null;
        if (clazz.isAnnotationPresent(Table.class)) {
            name = clazz.getAnnotation(Table.class).name();
        } else {
            name = clazz.getSimpleName() + "s";
        }
        return name;
    }

    public static ColumnDB getId(Class<?> clazz) throws Exception {
        String name;
        ColumnDB columnDB;
        List<Field> idFields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class)).collect(Collectors.toList());
        if (idFields.size() !=1) {
            throw new Exception("Class must have only one PK");
        } else {
            columnDB = new ColumnDB();
            if (idFields.get(0).isAnnotationPresent(Column.class)) {
                if (idFields.get(0).getAnnotation(Column.class).name().length()!=0) {
                    name = idFields.get(0).getAnnotation(Column.class).name();
                    System.out.println(name);
                }
                else
                    name = getNameTable(clazz) + "_" + idFields.get(0).getName();
            } else {
                name = getNameTable(clazz) + "_" + idFields.get(0).getName();
            }
            columnDB.setField(idFields.get(0));
            columnDB.setName(name);
            columnDB.setType(getSqlType(idFields.get(0)));
            columnDB.setNullable(false);
        }
        return columnDB;
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
