package com.practice.orm.annotation.entity;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Handler {
    private static Reflections reflections = new Reflections("NamePackage");

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

    public static Set<Field> getFieldsByClass(Class<?> clazz)
    {
        Set<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .collect(Collectors.toSet());
        return fields;
    }

    public static String getTableName(Class<?> clazz)
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

    public static Field getFieldNamedId(Class<?> clazz){
        Field idField = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst().get();
        return idField;
    }
}
