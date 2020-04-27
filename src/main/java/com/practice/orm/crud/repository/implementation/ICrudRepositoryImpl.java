package com.practice.orm.crud.repository.implementation;

import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.practice.orm.crud.repository.ICrudRepository;

public class ICrudRepositoryImpl<C> implements ICrudRepository<C, Integer> {

	String jdbcUrl = "jdbc:mysql://localhost:3306/test";
	String dbName = "user";
	String dbPassword = "password";

	String fullname = "Vasya Jopkin";
	String username = "Vasya";
	String email = "vasyan@jopa.mira";
	String password = "12345";

	// private DBUtil dbutil;
	// private QueryFormer queryFormer;

	// private ObjectList

	// public ICrudRepositoryImpl(DBUtil dbUtil, QueryFormer queryFormer) {
	// this.dbutil=dbUtil;
	// this.queryFormer=queryFormer;
	// }

	@Override
	public C add(C object) {

		try {
			Connection connection = DriverManager.getConnection(jdbcUrl, dbName, dbPassword);
			String sqlQuery = "INSERT INTO users (username, email, fullname, password)" + "VALUES(?,?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, fullname);
			preparedStatement.setString(4, password);

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
		String sqlQuery = "SELECT user FROM users WHERE id = " + index;
		try {
			Connection connection = DriverManager.getConnection(jdbcUrl, dbName, dbPassword);
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
