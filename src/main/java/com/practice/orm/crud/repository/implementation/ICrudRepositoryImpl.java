package com.practice.orm.crud.repository.implementation;

import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;
import com.practice.orm.annotation.entity.Column;
import com.practice.orm.annotation.entity.Entity;
import com.practice.orm.annotation.entity.DBHandlers.TableDB;
import com.practice.orm.annotation.entity.entityHandler.Handler;
import com.practice.orm.annotation.generator.Generator;
import com.practice.orm.annotation.generator.GeneratorHandler;
import com.practice.orm.crud.repository.ICrudRepository;
import com.practice.orm.crud.repository.test.Customer;
import com.practice.orm.db.utilDao.entiry.DBUtil;
import com.practice.orm.db.utilDao.entiry.DbKeys;
import com.practice.orm.db.utilDao.entiry.PropertyBundle;
import com.practice.orm.db.utilDao.entiry.QueryFormer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ICrudRepositoryImpl<C> implements ICrudRepository<C, Integer> {

	private DBUtil dbUtil;
	private QueryFormer queryFormer;
	private List<C> ObjectList;
	private PropertyBundle propertyBundle;
	private static String tableName;

	public ICrudRepositoryImpl() {
		ObjectList = new LinkedList<C>();
		propertyBundle = new PropertyBundle();
		try {
			dbUtil = DBUtil.getInstance(propertyBundle);
			queryFormer = QueryFormer.getInstance();
			queryFormer.setPropertyBundle(propertyBundle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public C add(C object) {
		//ошибка в порядке значений для преп стат
		try {
			Connection connection = dbUtil.getConnectionFromPool();
			String SqlQuery = makeSqlQuery(object);
			List<Field> fieldList = makeListOfFields(object);
			PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery);
			setFields(fieldList, preparedStatement, object);
			int rows = preparedStatement.executeUpdate();
			if (rows > 0) {
				System.out.println("A new object has been added successfully");
			}
			dbUtil.returnConnectionToPool(connection);
		} catch (Exception Ex) {
			Ex.printStackTrace();
		}

		return null;
	}

	@Override
	public C find(int id, Class clazz) {

		C foundObject = null;
		try {
			foundObject = (C) clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Connection connection = dbUtil.getConnectionFromPool();
			String SqlQuery = makeSqlQuery(clazz, DbKeys.READ);
			PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery);
			preparedStatement.setObject(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			foundObject = returnObject(resultSet, foundObject, clazz);
			dbUtil.returnConnectionToPool(connection);
		} catch (Exception Ex) {
			Ex.printStackTrace();
		}
		return foundObject;
	}

	@Override
	public boolean modify(int id, C obj) {
		try {
			Connection connection = dbUtil.getConnectionFromPool();
			C objectToUpdate = find(id, obj.getClass());
			String sqlQuery = makeSqlQuery(obj.getClass(), DbKeys.UPDATE);
			PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
			Map<Class<?>, String> namesOfTables = Handler.getNamesTable(Handler.getClassesNamedEntity());
			tableName = namesOfTables.get(obj.getClass());
			List<String> columns = Handler.getTable().get(tableName);
			List<Field> listOfFields = makeListOfFields(objectToUpdate);
			System.out.println(columns);
			for (int i = 0; i < listOfFields.size(); i++) {
				Field field = listOfFields.get(i);
				Optional<String> columnOptional = columns.stream().filter(column -> column.equalsIgnoreCase(field.getName())).findFirst();
				field.setAccessible(true);
				if (columnOptional.isPresent() && !columnOptional.get().equalsIgnoreCase(columns.get(0))) {
					preparedStatement.setObject( (i + 1), field.get(objectToUpdate));
				}
			}
//			preparedStatement.setObject(listOfFields.size(),listOfFields.get(0));
			System.out.println(preparedStatement);
			int rows = preparedStatement.executeUpdate();
			if (rows > 0) {
				System.out.println("A new object has been modified successfully");
			}
			dbUtil.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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
		try(ResultSet resultSet = preparedStatement.executeQuery()) {
			while (resultSet.next()) {
				C obj = this.getObjectFromResultSet(resultSet, clazz);
				foundObjects.add(obj);
			}
		}
		preparedStatement.close();
		dbUtil.returnConnectionToPool(connection);
		return foundObjects;
	}

	private String makeSqlQuery(C obj) {
		Handler.addClass(obj.getClass());
		GeneratorHandler.getInstance().setAnnotatedClasses(Handler.getClasses());
		GeneratorHandler.getInstance().buildTablesCounterGenerator();
		Map<Class<?>, String> namesOfTables = Handler.getNamesTable(Handler.getClassesNamedEntity());
		tableName = namesOfTables.get(obj.getClass());
		queryFormer.setTablesAndColumns(Handler.getTable());
		queryFormer.setPropertyBundle(propertyBundle);
		queryFormer.formQueriesForAllTables();
		String sqlQuery = queryFormer.getQuery(tableName, DbKeys.CREATE);
		return sqlQuery;
	}

	private String makeSqlQuery(Class clazz, String action) {
		Handler.addClass(clazz);
		Map<Class<?>, String> namesOfTables = Handler.getNamesTable(Handler.getClassesNamedEntity());
		tableName = namesOfTables.get(clazz);
		queryFormer.setTablesAndColumns(Handler.getTable());
		queryFormer.setPropertyBundle(propertyBundle);
		queryFormer.formQueriesForAllTables();
		String sqlQuery = queryFormer.getQuery(tableName, action);
		return sqlQuery;
	}

	private List<Field> makeListOfFields(C obj) {
		Set<TableDB> tableSet = Handler.getTablesDB();
		List<Field> fieldList = new ArrayList<Field>();
		for (TableDB table : tableSet) {
			if (obj.getClass().isAnnotationPresent(Entity.class)) {
				Field[] fields = obj.getClass().getDeclaredFields();
				for (Field field : fields) {
					if (field.isAnnotationPresent(Column.class)) {
						fieldList.add(field);
					}
				}
			}
		}
		return fieldList;
	}

	private void setFields(List<Field> fieldList, PreparedStatement preparedStatement, C obj)
			throws IllegalArgumentException, IllegalAccessException, SQLException {
		Object idValue = GeneratorHandler.getInstance().generateIdValue(tableName);
		for (int i = 0; i < fieldList.size(); i++) {
			Field field = fieldList.get(i);
			field.setAccessible(true);
			if (i == 0) {
				preparedStatement.setObject((i + 1), idValue);
				continue;
			}
			preparedStatement.setObject((i + 1), field.get(obj));
		}
	}

	private C returnObject(ResultSet resultSet, C foundObject, Class clazz) {
		Field[] fields = clazz.getDeclaredFields();
		try {
			if (resultSet.next()) {
				for (String column : Handler.getTable().get(tableName)) {
					if (column.equals(tableName + "_id")) {
						for (Field f : fields) {
							if (f.getName().equals("id")) {
								f.setAccessible(true);
								f.set(foundObject, resultSet.getInt(column));
							}
						}

						continue;
					}
					for (Field f : fields) {
						if (f.getType() == int.class && f.getName().equals(column)) {
							f.setAccessible(true);
							f.set(foundObject, resultSet.getInt(column));
						}

						if (f.getType() == String.class && f.getName().equals(column)) {
							f.setAccessible(true);
							f.set(foundObject, resultSet.getString(column));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return foundObject;
	}

	private C getObjectFromResultSet(ResultSet resultSet, Class clazz) throws IllegalAccessException, InstantiationException, NoSuchFieldException, SQLException {
		C obj = (C) clazz.newInstance();
		List<String> columns = Handler.getTable().get(tableName);
		for (String column : columns) {
			String fieldName = new String(column);
			if (fieldName.matches(tableName + "_.+")) {
				int charAt = fieldName.indexOf("_");
				fieldName = fieldName.substring(charAt + 1);
			}
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			Object value = resultSet.getObject(column);
			field.set(obj, value);
		}
		return obj;
	}
}
