package com.practice.orm.annotation.entity.entityHandler;

import com.practice.orm.annotation.entity.DBHandlers.ColumnForDB;
import com.practice.orm.annotation.entity.Entity;
import com.practice.orm.annotation.entity.Id;
import com.practice.orm.annotation.entity.Column;
import com.practice.orm.annotation.entity.Table;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class Handler {
    private static Reflections reflections;

    public static void run(Class<?> clazz)
    {
        reflections = new Reflections(clazz.getPackageName());
    }

    public static Set<Class<?>> getClassesNamedEntity() {
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Entity.class);
        return classes;
    }

    public static Set<Field> getFieldsNamedByAnnotation(Class<?> classes,
                                                        Class<? extends Annotation> annotation)
    {
        Set<Field> fields = Arrays.stream(classes.getDeclaredFields())
                .filter(field ->field.isAnnotationPresent(annotation))
                .peek(field -> field.setAccessible(true))
                .collect(Collectors.toSet());
        return fields;
    }

    public static Set<ColumnForDB> getColumns(Class<?> clazz)
    {
        Set<ColumnForDB> columnsForDB = new HashSet<>();
        for (Field f :
                getFieldsNamedByAnnotation(clazz, Column.class)) {
            ColumnForDB columnForDB = new ColumnForDB();
            if (f.isAnnotationPresent(Column.class)) {
                columnForDB.setName(f.getAnnotation(Column.class).name());
                if (f.getAnnotation(Column.class).nullable()==true)
                    columnForDB.setNullable(true);
                else
                    columnForDB.setNullable(false);
            }
            //Could Add column without @Column
            columnForDB.setField(f);
            columnForDB.setType(getSqlType(f));
            columnsForDB.add(columnForDB);
        }
        return columnsForDB;
    }
    public static String getNameTable(Class<?> clazz)
    {
        String name = null;
        if (clazz.isAnnotationPresent(Table.class))
        {
            name = clazz.getAnnotation(Table.class).name();
        }
        else
        {
            name = clazz.getSimpleName();
        }
        return name;
    }
    
    public static Map<Class<?>,String> getNamesTable(Set<Class<?>> classes)
    {
        Map<Class<?>,String> names = new HashMap<Class<?>, String>();
        for (Class<?> c :
                classes) {
            if (c.isAnnotationPresent(Table.class))
            {
                names.put(c,c.getAnnotation(Table.class).name());
            }else
            {
                names.put(c,c.getSimpleName());
            }
        }
        return names;
    }

    public static ColumnForDB getId(Class<?> clazz){
        Field idField = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst().get();
        ColumnForDB columnForDB = new ColumnForDB();
        columnForDB.setField(idField);
        columnForDB.setName(idField.getName());
        columnForDB.setType(getSqlType(idField));
        columnForDB.setNullable(true);
        return columnForDB;
    }

    private static String getSqlType(Field field)
    {
        String javaType = field.getType().getSimpleName().toUpperCase();
        switch (javaType)
        {
            case "SHORT":
                return "SMALLINT";
            case "INT":
            case "INTEGER":
                return "INTEGER";
            case "LONG":
                return "BIGINT";
            case "BOOLEAN":
                return "BOOLEAN";
            case "DOUBLE":
                return "DOUBLE";
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
