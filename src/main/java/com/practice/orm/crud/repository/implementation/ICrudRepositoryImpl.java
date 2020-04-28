package com.practice.orm.crud.repository.implementation;

import java.util.LinkedList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
			// queryFormer.setTablesAndColumns(tablesAndColumns);
			String sqlQuery = queryFormer.getQuery("add something", "add something");

			PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setString(1, "add something");
			preparedStatement.setString(2, "add something");
			preparedStatement.setString(3, "add something");
			preparedStatement.setString(4, "add something");

			int rows = preparedStatement.executeUpdate();

			if (rows > 0) {
				System.out.println("A new user has been added successfully");
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
