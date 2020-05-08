package com.practice.orm.annotation.entity.entityHandler;

import com.practice.orm.annotation.entity.Column;
import com.practice.orm.annotation.entity.DBHandlers.ColumnDB;
import com.practice.orm.annotation.entity.DBHandlers.TableDB;
import com.practice.orm.annotation.entity.Id;
import com.practice.orm.annotation.entity.Table;
import com.practice.orm.annotation.entity.entityHandler.exceptions.NotFoundAnnotatedClass;
import com.practice.orm.annotation.generator.Generator;
import com.practice.orm.annotation.relationalAnotation.relationalHandler.RelationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class Handler {

    private static final List<Class> classes = new LinkedList<>();
    private static final Set<TableDB> relationalTables = new HashSet<>();
    private static final List<Class<?>> beans = new ArrayList<>();
    private static final Map<String, Map<String, String>> beanFieldMap = new HashMap<>();

    static {

    }

    public static Set<TableDB> getRelationalTables() {
        getTablesDB();
        return relationalTables;
    }

    public static void addRelationTable(TableDB tableDB) {
        relationalTables.add(tableDB);
    }

    public static void addClass(Class<?> clazz) {
        if (!classes.contains(clazz))
            classes.add(clazz);
    }

    public static List<Class> getClasses() throws NotFoundAnnotatedClass {
        if (classes.isEmpty()) {
            throw new NotFoundAnnotatedClass("List is empty");
        } else {
            return classes;
        }
    }

    public static boolean isBean(Class<?> clazz) {
        getBeans();
        return beans.contains(clazz);
    }

    public static String getBeanMap(String tableName, String key) {
        String value = null;
        if (beanFieldMap.containsKey(tableName)
                && beanFieldMap.get(tableName).containsKey(key)) {
            value = beanFieldMap.get(tableName).get(key);
        }
        return value;
    }

    public static boolean hasBeanInside(String tableName) {
        return beanFieldMap.containsKey(tableName);
    }

    public static Map<String, String> getBeanMap(String tableName) {
        return beanFieldMap.get(tableName);
    }

    private static void getBeans() {
        for (Class<?> c :
                getClassesNamedEntity()) {
            Set<Field> fieldsNamedByAnnotation = getFieldsNamedByAnnotation(c, ColumnMarker.class);
            if (fieldsNamedByAnnotation.size() == 0) {
                if (!beans.contains(c))
                    beans.add(c);
            }
        }
    }


    public static Set<Class<?>> getClassesNamedEntity() {
        Set<Class<?>> setClasses = new HashSet<>();
        for (Class<?> clazz :
                classes) {
            setClasses.add(clazz);
        }
        return setClasses;
    }

    public static Class getClassByTableName(String tableName) {
        for (Class clazz :
                classes) {
            if (getNameTable(clazz).equals(tableName)) {
                return clazz;
            }
        }
        return null;
    }

    public static Map<String, List<String>> getTable() {
        Map<String, List<String>> table = new HashMap<>();
        for (Class cl : getClassesNamedEntity()) {
            TableDB t = getTableDB(cl);
            List<String> columnsName = new ArrayList<>();
            columnsName.add(t.getPrimaryKey().getName());
            for (ColumnDB c : t.getColumnDBS()) {
                columnsName.add(c.getName());
            }
            table.put(t.getTableName(), columnsName);
        }
        for (String tableName :
                table.keySet()) {
            getColumnMarker(table, tableName);
        }
        return table;
    }

    private static Map<String, List<String>> getColumnMarker(Map<String, List<String>> table, String tableName) {
        Class cl = getClassByTableName(tableName);
        List<String> columns = table.get(tableName);
        for (Field f :
                getFieldsNamedByAnnotation(cl, ColumnMarker.class)) {
            columns.add(f.getName());
            if (!beanFieldMap.containsKey(tableName)) {
                beanFieldMap.put(tableName, new HashMap<>());
            }
            String beanMap = getNameTable(f.getType()) + "_id";
            if (!beanFieldMap.get(tableName).containsKey(beanMap)) {
                beanFieldMap.get(tableName).put(beanMap, f.getName());
            }
            if (!beanFieldMap.get(tableName).containsKey(f.getName())) {
                beanFieldMap.get(tableName).put(f.getName(), beanMap);
            }
        }
        return table;
    }


    public static TableDB getTableDB(Class<?> clazz) {
        TableDB tableDB = new TableDB();
        tableDB.setColumnDBS(getColumns(clazz));
        try {
            tableDB.setPrimaryKey(getId(clazz));
            tableDB.setPKAutoIncrement(tableDB.getPrimaryKey().getField().isAnnotationPresent(Generator.class));
            tableDB.setForeignKey(RelationHandler.getForeignKey(clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tableDB.setTableName(getNameTable(clazz));
        tableDB.setClazz(clazz);
        return tableDB;
    }

    public static void getTablesDB() {

        for (Class<?> clazz : getClassesNamedEntity()) {
            if (relationalTables.contains(getTableDB(clazz))) {
                continue;
            } else {
                relationalTables.add(getTableDB(clazz));
            }
        }
    }

    public static Map<Class<?>, String> getNameTableByClass(Class<?> clazz) {
        Map<Class<?>, String> nameTable = new HashMap<>();
        nameTable.put(clazz, getNameTable(clazz));
        return nameTable;
    }

    public static Map<String, Field> getFieldByName(Class<?> clazz) {
        Map<String, Field> fieldMap = new HashMap<>();
        for (ColumnDB col :
                getColumns(clazz)) {
            fieldMap.put(col.getName(), col.getField());
        }
        return fieldMap;
    }

    public static Map<Class<?>, String> getNamesTable(Set<Class<?>> classes) {
        Map<Class<?>, String> names = new HashMap<Class<?>, String>();
        for (Class<?> clazz :
                classes) {
            names.put(clazz, getNameTable(clazz));
        }
        return names;
    }

    public static Set<Field> getFieldsNamedByAnnotation(Class<?> classes,
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
            columnDB.setNullable(field.getAnnotation(Column.class).nullable() == true);
            if (field.getAnnotation(Column.class).name() != "") {
                columnDB.setName(field.getName());
            } else {
                columnDB.setName(field.getAnnotation(Column.class).name());
            }
        }

        columnDB.setLength(field.getAnnotation(Column.class).length());
        columnDB.setField(field);
        columnDB.setType(getSqlType(field));
        return columnDB;
    }

    public static Set<ColumnDB> getColumns(Class<?> clazz) {
        Set<ColumnDB> columnsForDB = new HashSet<>();
        for (Field f :
                getFieldsNamedByAnnotation(clazz, Column.class)) {
            if (f.isAnnotationPresent(Id.class)) {
                continue;
            } else {
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
        if (idFields.size() != 1) {
            throw new Exception("Class must have only one PK");
        } else {
            columnDB = new ColumnDB();
            if (idFields.get(0).isAnnotationPresent(Column.class)) {
                if (idFields.get(0).getAnnotation(Column.class).name().length() != 0) {
                    name = idFields.get(0).getAnnotation(Column.class).name();
                    System.out.println(name);
                } else
                    name = getNameId(clazz, idFields.get(0));
            } else {
                name = getNameId(clazz, idFields.get(0));
            }
            columnDB.setField(idFields.get(0));
            columnDB.setName(name);
            columnDB.setType(getSqlType(idFields.get(0)));
            columnDB.setNullable(false);
        }
        return columnDB;
    }

    private static String getNameId(Class<?> clazz, Field field) {
        StringBuilder stringBuilder;
        if (clazz.isAnnotationPresent(Table.class)) {
            stringBuilder = new StringBuilder(getNameTable(clazz));
        } else {
            stringBuilder = new StringBuilder(getNameTable(clazz)
                    .substring(0, getNameTable(clazz).length() - 1));
        }
        stringBuilder.append("_");
        stringBuilder.append(field.getName());
        return stringBuilder.toString();
    }

    public static ColumnDB setNamePrimaryKey(Class<?> clazz, String name) throws Exception {
        ColumnDB id = getId(clazz);
        if (!name.isEmpty()) {
            id.setName(name);
        } else {
            id.setName(name + "_id");
        }
        return id;


    }

    public static Class getTypeClass(Field field) {
        Class type;
        if (Collection.class.isAssignableFrom(field.getType()))
            type = getGenericType(field.getGenericType(), 0);
        else
            type = field.getType();
        return type;
    }

    private static Class getGenericType(Type type, int numb) {
        return (Class) ((ParameterizedType) type).getActualTypeArguments()[numb];
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
