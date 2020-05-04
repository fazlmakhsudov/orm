package com.practice.orm.annotation.entity.entityHandler;

import com.practice.orm.annotation.entity.Column;
import com.practice.orm.annotation.entity.DBHandlers.ColumnDB;
import com.practice.orm.annotation.entity.DBHandlers.ForeignKey;
import com.practice.orm.annotation.entity.DBHandlers.TableDB;
import com.practice.orm.annotation.entity.Id;
import com.practice.orm.annotation.entity.Table;
import com.practice.orm.annotation.entity.entityHandler.exceptions.NotFoundAnnotatedClass;
import com.practice.orm.annotation.relationalAnotation.JoinColumn;
import com.practice.orm.annotation.relationalAnotation.ManyToMany;
import com.practice.orm.annotation.relationalAnotation.ManyToOne;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Types;
import java.util.*;
import java.util.stream.Collectors;

public class Handler {

    private static final List<Class> classes = new LinkedList<>();
    private static final Set<TableDB> relationalTables = new HashSet<>();

    public static Set<TableDB> getRelationalTables() {
        return relationalTables;
    }

    public static void addClass(Class<?> clazz) {
        classes.add(clazz);
    }


    public static List<Class> getClasses() throws NotFoundAnnotatedClass {
        if (classes.isEmpty()) {
            throw new NotFoundAnnotatedClass("List is empty");
        } else {
            return classes;
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
        try {
            tableDB.setPrimaryKey(getId(clazz));
            tableDB.setForeignKey(getForeignKey(clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tableDB.setTableName(getNameTable(clazz));
        tableDB.setClazz(clazz);
        return tableDB;
    }


    public static Set<TableDB> getTablesDB() {
        Set<TableDB> tableDBS = new HashSet<>();
        Set<Class<?>> classes = getClassesNamedEntity();
        for (Class<?> clazz : classes) {
            tableDBS.add(getTableDB(clazz));
//            if (getTableDB(clazz).getForeignKey()==null)
//            {
//                relationalTables.add(getTableDB(clazz));
//            }
//            else {
//                tableDBS.add(getTableDB(clazz));
//            }
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

    private static Set<ColumnDB> getColumns(Class<?> clazz) {
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

    public static Set<ForeignKey> getForeignKey(Class<?> clazz) throws Exception {
        Field[] declaredFields = clazz.getDeclaredFields();
        Set<ForeignKey> foreignKey = new HashSet<>();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(ManyToOne.class)) {
                foreignKey.add(getForeignKeyForManyToOne(field));
            }
            if (field.isAnnotationPresent(ManyToMany.class))
            {
                relationalTables.add(getForeignKeyForManyToMany(field,clazz));
            }

        }
        return foreignKey;
    }

    public static TableDB getForeignKeyForManyToMany(Field field,Class<?> clazz) throws Exception {
        TableDB tableDB = new TableDB();
        Class genericType=getTypeClass(field);
        if (field.isAnnotationPresent(ManyToMany.class))
        {
            tableDB.setTableName(clazz.getSimpleName()+"_"+genericType.getSimpleName());
            tableDB.setPrimaryKey(setNamePrimaryKey(clazz,tableDB.getTableName()));
            Set<ForeignKey> foreignKeys = new HashSet<>();
            foreignKeys.add(createForeignKey(clazz,""));
            foreignKeys.add(createForeignKey(genericType,""));
            tableDB.setForeignKey(foreignKeys);
        }
        return tableDB;
    }
    private static ForeignKey createForeignKey(Class<?> clazz,String name) throws Exception {
        ColumnDB columnDB = getId(clazz);
        ForeignKey foreignKey = new ForeignKey();
        foreignKey.setField(columnDB.getField());
        foreignKey.setType(columnDB.getType());
        foreignKey.setNullable(columnDB.getNullable());
        foreignKey.setLength(columnDB.getLength());
        foreignKey.setClazz(clazz);
        foreignKey.setName(columnDB.getName());
        foreignKey.setNameTableTo(getNameTable(clazz));
        if (!name.isEmpty()) {
            foreignKey.setNameColumnTo(name);
        }
        else
        {
            foreignKey.setNameColumnTo(columnDB.getName());
        }
        return foreignKey;
    }

    private static ColumnDB setNamePrimaryKey(Class<?> clazz, String name) throws Exception {
        ColumnDB id = getId(clazz);
        id.setName(name+"_id");
        return id;


    }
    private static Class getTypeClass(Field field)
    {
        Class type;
        if (Collection.class.isAssignableFrom(field.getType()))
            type = getGenericType(field.getGenericType(), 0);
         else
            type = field.getType();
         return type;
    }

    private static ForeignKey getForeignKeyForManyToOne(Field field) throws Exception {
        ForeignKey foreignKey = new ForeignKey();
        foreignKey.setClazz(getTypeClass(field));
//        if (Collection.class.isAssignableFrom(field.getType())) {
//            foreignKey.setClazz(getGenericType(field.getGenericType(), 0));
//        } else {
//            foreignKey.setClazz(field.getType());
//        }
        if (field.getAnnotation(ManyToOne.class).mappedBy()== "") {
            if (field.isAnnotationPresent(JoinColumn.class)) {
                if (field.getAnnotation(JoinColumn.class).name() == "") {
                    foreignKey.setName(field.getName() + "_fk");
                } else {
                    foreignKey.setName(field.getAnnotation(JoinColumn.class).name());
                }
            } else {
                foreignKey.setName(getId(foreignKey.getClazz()).getName());
            }
        }
        else
        {
            foreignKey.setName(field.getName());
        }
        foreignKey.setNameTableTo(getNameTable(foreignKey.getClazz()));
        foreignKey.setType(getId(foreignKey.getClazz()).getType());
        foreignKey.setDescription(field.getType().getSimpleName());
        foreignKey.setNameColumnTo(getId(foreignKey.getClazz()).getName());
        foreignKey.setField(field);
        foreignKey.setNullable(false);
        return foreignKey;
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
