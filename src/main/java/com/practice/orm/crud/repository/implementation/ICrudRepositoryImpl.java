package com.practice.orm.crud.repository.implementation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.practice.orm.annotation.entity.Column;
import com.practice.orm.annotation.entity.Entity;
import com.practice.orm.annotation.entity.DBHandlers.TableDB;
import com.practice.orm.annotation.entity.entityHandler.Handler;
import com.practice.orm.crud.repository.ICrudRepository;
import com.practice.orm.db.utilDao.entiry.DBUtil;
import com.practice.orm.db.utilDao.entiry.PropertyBundle;
import com.practice.orm.db.utilDao.entiry.QueryFormer;

public class ICrudRepositoryImpl<C> implements ICrudRepository<C, Integer> {

	private DBUtil dbUtil;
	private QueryFormer queryFormer;
	private List<C> ObjectList;
	private PropertyBundle propertyBundle;

	public ICrudRepositoryImpl(DBUtil dbUtil, QueryFormer queryFormer) {
		this.dbUtil = dbUtil;
		this.queryFormer = queryFormer;
		ObjectList = new LinkedList<C>();
		propertyBundle = new PropertyBundle("path-template");

		try {
			dbUtil = DBUtil.getInstance(propertyBundle);
			queryFormer = QueryFormer.getInstance();
			queryFormer.setPropertyBundle(propertyBundle);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public C add(C object) {

		try {

			Connection connection = dbUtil.getConnectionFromPool();
			String tableName = object.getClass().getSimpleName();
			String sqlQuery = queryFormer.getQuery(tableName, DBCase.create());

			PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
			// ----------------------------------------------------------------------------
			Set<TableDB> tableSet = Handler.getTablesDB();
			List<Field> fieldList = new ArrayList<Field>();
			for (TableDB table : tableSet) {
				if (object.getClass().isAnnotationPresent(Entity.class)) {
					Field[] fields = object.getClass().getDeclaredFields();
					for (Field field : fields) {
						if (field.isAnnotationPresent(Column.class)) {
							fieldList.add(field);
						}
					}
				}
			}
			// ----------------------------------------------------------------------------

			int fieldCounter = 1;
			for (Field field : fieldList) {
				if (field.getType() == String.class) {
					preparedStatement.setString(fieldCounter++, (String) field.get(object));
				} else if (field.getType() == Integer.class) {
					preparedStatement.setInt(fieldCounter++, (Integer) field.get(object));
				}
			}
			// -----------------------------------------------------------------------------

			int rows = preparedStatement.executeUpdate();

			if (rows > 0) {
				System.out.println("A new object has been added successfully");
			}

			connection.close();
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}

		return null;
	}

	@Override
	public C find(int id) {
		String index = String.valueOf(id);
		String sqlQuery = queryFormer.getQuery("", "");
		try {
			Connection connection = dbUtil.getConnectionFromPool();
			PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
			ResultSet resultSet = preparedStatement.executeQuery(sqlQuery);

			while (resultSet.next()) {
				String userName = resultSet.getString(2);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
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

}
