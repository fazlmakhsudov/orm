package com.practice.orm.annotation.generator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GeneratorHandler {
    private static GeneratorHandler generatorHandler;
     private Map<String, Integer> tableCounter;
     private List<Class> annotatedClasses;
     private final static Logger logger = Logger.getLogger("GeneratorHandler.class");

     private GeneratorHandler() {
         this.tableCounter = new HashMap<>();
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

    public Map<String, Integer> buildTablesCounter() {
        return annotatedClasses.stream()
                .collect(Collectors.toMap(cl -> getTableName(cl), cl -> initializeCounterForTable(cl)));
    }

    private String getTableName(Class cl) {
        Annotation table = cl.getAnnotation(Table.class);
        String tableName = cl.getSimpleName();
        if (table != null && !((Table) table).tableName().isBlank()) {
            tableName = ((Table) table).tableName();
        }
        tableName = tableName.trim().toLowerCase();
        return tableName;
    }

    private Integer initializeCounterForTable(Class cl) {
         Integer counter = null;
         Field[] fields = cl.getDeclaredFields();
         if (generatorHandler.getAnnotatedField(fields).isPresent()) {
             counter = 1;
         }
         return counter;
    }

    private Optional<Field> getAnnotatedField(Field[] fields) {
        return Arrays.stream(fields).filter(field -> field.getAnnotation(Generator.class) != null)
                .filter(field -> field.getAnnotation(Id.class) != null)
                .findFirst();
    }

    public Integer generateIdValue(String tableName) {
         Integer value = null;
         if (generatorHandler.tableCounter.containsKey(tableName)) {
             value = generatorHandler.tableCounter.get(tableName);
             generatorHandler.tableCounter.replace(tableName,(value + 1));
         }
         return value;
    }
}
