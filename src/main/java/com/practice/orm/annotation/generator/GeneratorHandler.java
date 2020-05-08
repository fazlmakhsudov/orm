package com.practice.orm.annotation.generator;

import com.practice.orm.annotation.entity.Id;
import com.practice.orm.annotation.entity.Table;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GeneratorHandler {
    private static GeneratorHandler generatorHandler;
    public Map<String, String> tableCounterType;
    public Map<String, Integer> countersForTable;
    private List<Class> annotatedClasses;
    private final static Logger logger = Logger.getLogger("GeneratorHandler.class");

    private GeneratorHandler() {
        this.tableCounterType = new HashMap<>();
        this.countersForTable = new HashMap<>();
        logger.info("GeneratorHandler has been instantiated.");
    }

    public static GeneratorHandler getInstance() {
        if (generatorHandler == null) {
            generatorHandler = new GeneratorHandler();
        }
        return generatorHandler;
    }

    public void setAnnotatedClasses(List<Class> annotatedClasses) {
        generatorHandler.annotatedClasses = annotatedClasses;
        logger.log(Level.INFO, "setAnnatatedClasses() with parameter:\n {0}",
                new String[]{annotatedClasses.toString()});
    }

    public void buildTablesCounterGenerator() {
        boolean flag = generatorHandler.annotatedClasses.stream()
                .anyMatch(cl -> {
                    return generatorHandler.countersForTable.containsKey(getTableName(cl));
                });
        if (flag) {
            return;
        }
        initializeCounterForTables();
        generatorHandler.tableCounterType = generatorHandler.annotatedClasses.stream()
                .filter(cl -> getAnnotatedField(cl.getDeclaredFields()).isPresent())
                .collect(Collectors.toMap(cl -> getTableName(cl), cl -> initializeTableCounterType(cl)));
    }

    private void initializeCounterForTables() {
        generatorHandler.countersForTable = generatorHandler.annotatedClasses.stream()
                .filter(cl -> getAnnotatedField(cl.getDeclaredFields()).isPresent())
                .collect(Collectors.toMap(cl -> getTableName(cl), cl -> 1));
    }

    private String getTableName(Class cl) {
        Annotation table = cl.getAnnotation(Table.class);
        String tableName = cl.getSimpleName();
        if (table != null && !((Table) table).name().isBlank()) {
            tableName = ((Table) table).name();
        }
        tableName = tableName.trim();
        return tableName;
    }

    private String initializeTableCounterType(Class cl) {
        String counter = null;
        Field[] fields = cl.getDeclaredFields();
        Optional<Field> optionalField = generatorHandler.getAnnotatedField(fields);
        if (optionalField.isPresent()) {
            counter = optionalField.get().getType().getSimpleName();
        }
        return counter;
    }

    private Optional<Field> getAnnotatedField(Field[] fields) {
        return Arrays.stream(fields).filter(field -> field.getAnnotation(Generator.class) != null)
                .filter(field -> field.getAnnotation(Id.class) != null).findFirst();
    }

    public Object generateIdValue(String tableName) {
        Object value = null;
        if (generatorHandler.tableCounterType.containsKey(tableName)) {
            value = generatorHandler.getValueForTable(tableName);
        }
        return value;
    }

    private Object getValueForTable(String tableName) {
        Object value = null;
        int currentValue = generatorHandler.countersForTable.get(tableName);
        String fieldType = generatorHandler.tableCounterType.get(tableName);
        if (fieldType.equalsIgnoreCase("int")) {
            value = currentValue;
        } else {
            value = tableName.toLowerCase() + "#" + currentValue;
        }
        generatorHandler.countersForTable.replace(tableName, currentValue + 1);
        return value;
    }

    public boolean isContainedTable(String tableName) {
        return generatorHandler.tableCounterType.containsKey(tableName);
    }
}
