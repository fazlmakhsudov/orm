package com.practice.orm.db.utilDao.entiry;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionPool {
	// JDBC Driver Name & Database URL
	private final String JDBC_DRIVER;
	private final String JDBC_DB_URL;
	private final static Logger logger = Logger.getLogger("ConnectionPool.class");

	// JDBC Database Credentials
	private final String JDBC_USER;
	private final String JDBC_PASSWORD;

	private static GenericObjectPool gPool = null;

	public ConnectionPool(PropertyBundle propertyBundle) {
		this.JDBC_DRIVER = propertyBundle.getQuery("jdbcDriver");
		this.JDBC_DB_URL = propertyBundle.getQuery("jdbcUrl") + "/" + propertyBundle.getQuery("jdbcDatabase");
		this.JDBC_USER = propertyBundle.getQuery("jdbcUserName");
		this.JDBC_PASSWORD = propertyBundle.getQuery("jdbcPassword");
		logger.log(Level.INFO, "ConnectionPool object has been initialised with params ({0}, {1}, {2}, {3})",
				new String[] { logger.getName(), this.JDBC_DRIVER, this.JDBC_DB_URL, this.JDBC_USER,
						this.JDBC_PASSWORD });
	}

	@SuppressWarnings("unused")
	public DataSource setUpPool(int maxTotalConn) throws Exception {
		Class.forName(JDBC_DRIVER);

		// Creates an Instance of GenericObjectPool That Holds Our Pool of Connections
		// Object!
		gPool = new GenericObjectPool();
		gPool.setMaxActive(maxTotalConn);

		// Creates a ConnectionFactory Object Which Will Be Use by the Pool to Create
		// the Connection Object!
		ConnectionFactory cf = new DriverManagerConnectionFactory(JDBC_DB_URL, JDBC_USER, JDBC_PASSWORD);

		// Creates a PoolableConnectionFactory That Will Wraps the Connection Object
		// Created by the ConnectionFactory to Add Object Pooling Functionality!
		PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, gPool, null, null, false, true);
		return new PoolingDataSource(gPool);
	}

	public GenericObjectPool getConnectionPool() {
		return gPool;
	}

	// This Method Is Used To Print The Connection Pool Status
	public String printDbStatus() {
		return "{ Max.: " + getConnectionPool().getMaxActive() + "; Active: " + getConnectionPool().getNumActive() + ";"
				+ " Idle: " + getConnectionPool().getNumIdle() + " }";
	}
}
