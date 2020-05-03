package com.practice.orm.crud.repository.implementation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.practice.orm.annotation.entity.Column;
import com.practice.orm.annotation.entity.Entity;
import com.practice.orm.annotation.entity.Table;
import com.practice.orm.annotation.entity.DBHandlers.TableDB;
import com.practice.orm.annotation.entity.entityHandler.Handler;
import com.practice.orm.annotation.generator.GeneratorHandler;
import com.practice.orm.crud.repository.ICrudRepository;
import com.practice.orm.db.utilDao.entiry.DBUtil;
import com.practice.orm.db.utilDao.entiry.DbKeys;
import com.practice.orm.db.utilDao.entiry.Path;
import com.practice.orm.db.utilDao.entiry.PropertyBundle;
import com.practice.orm.db.utilDao.entiry.QueryFormer;

public class ICrudRepositoryImpl<C> implements ICrudRepository<C, Integer> {

	private DBUtil dbUtil;
	private QueryFormer queryFormer;
	private List<C> ObjectList;
	private PropertyBundle propertyBundle;

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
	public C find(int id) {

		return null;
	}

	@Override
	public boolean modify(C object) {

		return false;
	}

	@Override
	public boolean remove(C object) {

		return false;
	}

	@Override
	public List<C> findAll() {

		return null;
	}

	private String makeSqlQuery(C obj) {
		Handler.addClass(obj.getClass());
		Map<Class<?>, String> namesOfTables = Handler.getNamesTable(Handler.getClassesNamedEntity());
		String tableName = namesOfTables.get(obj.getClass());
		queryFormer.setTablesAndColumns(Handler.getTable());
		queryFormer.setPropertyBundle(propertyBundle);
		queryFormer.formQueriesForAllTables();
		String sqlQuery = queryFormer.getQuery(tableName, DbKeys.CREATE);
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
		int fieldCounter = 1;
		for (Field field : fieldList) {
			// if (valueId == null) {
			// continue;
			// } else if (valueId != null) {
			// preparedStatement.setObject(fieldCounter++, valueId);
			// continue;
			// }
			preparedStatement.setObject(fieldCounter++, field.get(obj));

		}
	}

	public static void main(String[] args) {
		ICrudRepositoryImpl<Customer> crudRepo = new ICrudRepositoryImpl<Customer>();
		crudRepo.add(new Customer("Jim", "Halpert", 30));
	}
}
