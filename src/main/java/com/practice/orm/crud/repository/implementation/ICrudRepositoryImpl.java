package com.practice.orm.crud.repository.implementation;

import com.practice.orm.annotation.entity.entityHandler.Handler;
import com.practice.orm.annotation.entity.entityHandler.exceptions.NotFoundAnnotatedClass;
import com.practice.orm.annotation.generator.GeneratorHandler;
import com.practice.orm.crud.dispatcher.ICrudServiceDispatcher;
import com.practice.orm.crud.repository.ICrudRepository;
import com.practice.orm.db.utilDao.entiry.DBUtil;
import com.practice.orm.db.utilDao.entiry.DbKeys;
import com.practice.orm.db.utilDao.entiry.PropertyBundle;
import com.practice.orm.db.utilDao.entiry.QueryFormer;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ICrudRepositoryImpl<C> implements ICrudRepository<C, Integer> {

    private DBUtil dbUtil;
    private QueryFormer queryFormer;
    private final PropertyBundle propertyBundle;
    private static String tableName;

    public ICrudRepositoryImpl() {
        propertyBundle = new PropertyBundle();
        try {
            dbUtil = DBUtil.getInstance(propertyBundle);
            queryFormer = QueryFormer.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean add(C object) {
        boolean flag = false;
        try {
            Connection connection = dbUtil.getConnectionFromPool();
            String SqlQuery = makeSqlQuery(object);
            Map<String, Field> fieldList = makeListOfFields(object);
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery);
            setFields(fieldList, preparedStatement, object);

            flag = ICrudServiceDispatcher.remarkAddedObject(object);
            if (!flag) {
                return flag;
            }

            flag = preparedStatement.executeUpdate() > 0;
            if (flag) {
                System.out.println("A new object has been added successfully");
            }
            dbUtil.returnConnectionToPool(connection);
        } catch (Exception Ex) {
            Ex.printStackTrace();
        }

        return flag;
    }

    @Override
    public C find(Object id, Class clazz) {
        Connection connection = dbUtil.getConnectionFromPool();
        C foundObject = null;
        try {
            String sqlQuery = makeSqlQuery(clazz, DbKeys.READ);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                foundObject = this.getObjectFromResultSet(resultSet, clazz);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        dbUtil.returnConnectionToPool(connection);
        return foundObject;
    }

    @Override
    public boolean modify(Object id, C obj) {
        boolean flag = false;
        try {
            Connection connection = dbUtil.getConnectionFromPool();
            PreparedStatement preparedStatement = updateIntermidiateOperation(id, obj, connection);
            flag = preparedStatement.executeUpdate() > 0;
            if (flag) {
                System.out.println("A new object has been modified successfully");
            }
            dbUtil.returnConnectionToPool(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean remove(Object id, Class clazz) throws SQLException {
        Connection connection = dbUtil.getConnectionFromPool();
        String SqlQuery = makeSqlQuery(clazz, DbKeys.DELETE);
        PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery);
        preparedStatement.setObject(1, id);
        boolean flag = preparedStatement.executeUpdate() > 0;
        dbUtil.returnConnectionToPool(connection);
        return flag;
    }

    @Override
    public List<C> findAll(Class clazz) throws SQLException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        Connection connection = dbUtil.getConnectionFromPool();
        String sqlQuery = makeSqlQuery(clazz, DbKeys.READ_ALL);
        List<C> foundObjects = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                C obj = this.getObjectFromResultSet(resultSet, clazz);
                foundObjects.add(obj);
            }
        }
        preparedStatement.close();
        dbUtil.returnConnectionToPool(connection);
        return foundObjects;
    }

    private PreparedStatement updateIntermidiateOperation(Object id, C obj, Connection connection)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        String sqlQuery = makeSqlQuery(obj.getClass(), DbKeys.UPDATE);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        Map<Class<?>, String> namesOfTables = Handler.getNamesTable(Handler.getClassesNamedEntity());
        tableName = namesOfTables.get(obj.getClass());
        Map<String, Field> fieldMap = makeListOfFields(obj);
        List<String> fieldOrder = QueryFormer.getInstance().getFieldOrder(tableName);
        for (int i = 1; i < fieldOrder.size(); i++) {
            String column = fieldOrder.get(i);
            Field field = fieldMap.get(column);
            field.setAccessible(true);
            Object fieldValue = getBeanIdFromField(field, obj);
            preparedStatement.setObject(i, fieldValue);
        }
        preparedStatement.setObject(fieldMap.size(), id);
        return preparedStatement;
    }

    private String makeSqlQuery(C obj) throws NotFoundAnnotatedClass {
        Map<Class<?>, String> namesOfTables = Handler.getNamesTable(Handler.getClassesNamedEntity());
        tableName = namesOfTables.get(obj.getClass());
        String sqlQuery = queryFormer.getQuery(tableName, DbKeys.CREATE);
        return sqlQuery;
    }

    private String makeSqlQuery(Class clazz, String action) {
        Handler.addClass(clazz);
        Map<Class<?>, String> namesOfTables = Handler.getNamesTable(Handler.getClassesNamedEntity());
        tableName = namesOfTables.get(clazz);
        String sqlQuery = queryFormer.getQuery(tableName, action);
        return sqlQuery;
    }

    private Map<String, Field> makeListOfFields(C obj) throws NoSuchFieldException {
        Map<String, Field> fieldMap = new HashMap<>();
        List<String> columns = Handler.getTable().get(tableName);
        for (String column : columns) {
            String fieldName = column;
            if (column.matches(tableName + ".+")) {
                int charAt = fieldName.indexOf("_");
                fieldName = fieldName.substring(charAt + 1);
            }
            Field field = obj.getClass().getDeclaredField(fieldName);
            fieldMap.put(column, field);
        }
        return fieldMap;
    }

    private void setFields(Map<String, Field> fieldMap, PreparedStatement preparedStatement, C obj)
            throws IllegalArgumentException, IllegalAccessException, SQLException, NoSuchFieldException {
        Object idValue = GeneratorHandler.getInstance().generateIdValue(tableName);
        List<String> fieldOrder = QueryFormer.getInstance().getFieldOrder(tableName);
        for (int i = 0; i < fieldOrder.size(); i++) {
            String column = fieldOrder.get(i);
            Field field = fieldMap.get(column);
            field.setAccessible(true);
            Object fieldValue = getBeanIdFromField(field, obj);
            if (i == 0) {
                fieldValue = idValue;
                field.set(obj, idValue);
            }
            preparedStatement.setObject((i + 1), fieldValue);
        }
    }

    private String isFieldBeanColumn(String column, Class cl) {
        String tableName = Handler.getNameTable(cl);
        String value = Handler.getBeanMap(tableName, column);
        if (value != null) {
            column = value;
        }
        return column;
    }

    private Object getBeanIdFromField(Field field, C obj) throws IllegalAccessException, NoSuchFieldException {
        boolean flag = Handler.isBean(field.getType());
        Object value = field.get(obj);
        if (!flag) {
            return value;
        }
        Field idField = getIdFieldFromBean(field);
        return idField.get(field.get(obj));
    }

    private Field getIdFieldFromBean(Field field) throws NoSuchFieldException {
        Class fieldType = field.getType();
        String tableName = Handler.getNameTable(fieldType);
        String columnId = QueryFormer.getInstance().getColumnId(tableName);
        if (columnId.matches(tableName + ".+")) {
            int charAt = columnId.indexOf("_");
            columnId = columnId.substring(charAt + 1);
        }
        Field idField = fieldType.getDeclaredField(columnId);
        idField.setAccessible(true);
        return idField;
    }

    private C getObjectFromResultSet(ResultSet resultSet, Class clazz) throws IllegalAccessException, InstantiationException, NoSuchFieldException, SQLException {
        C obj = (C) clazz.newInstance();
        List<String> columns = Handler.getTable().get(tableName);
        for (String column : columns) {
            String fieldName = column;
            if (fieldName.matches(tableName + "_.+")) {
                int charAt = fieldName.indexOf("_");
                fieldName = fieldName.substring(charAt + 1);
            }
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            column = isFieldBeanColumn(column, clazz);
            Object value = resultSet.getObject(column);
            boolean flag = Handler.isBean(field.getType());
            // you can latter implement lazy load
            if (flag) {
                Field idField = getIdFieldFromBean(field);
                Object bean = field.getType().newInstance();
                idField.set(bean, value);
                field.set(obj, bean);
            } else {
                field.set(obj, value);
            }
        }
        return obj;
    }
}
