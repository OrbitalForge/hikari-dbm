package com.orbitalforge.hikari.dbm.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import com.orbitalforge.hikari.dbm.platform.AbstractDbPlatform;
import com.orbitalforge.hikari.dbm.platform.H2Platform;
import com.orbitalforge.hikari.dbm.schemaframework.SchemaManager;
import com.zaxxer.hikari.HikariDataSource;

/**
 * A database service represents the connection lifecycle in addition
 * to other very important information pertaining to the database itself.
 * 
 * This will also configure platform level information. The platform controls
 * how we speak to a database. Which datatypes, queries, etc to use against
 * a given database implementation.
 */
public class AbstractDbService {
	/**
	 * Internal Connection
	 */
	protected final HikariDataSource ds = new HikariDataSource();
	
	public DataSource getDataSource() {
		return ds;
	}
	
	public AbstractDbService() {
        ds.setMaximumPoolSize(20);
        ds.setAutoCommit(false);
        
        Properties prop = new Properties();
		String propFileName = "db.properties";
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

		try {
			prop.load(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        ds.setDriverClassName(prop.getProperty("db.classname"));
        ds.setJdbcUrl(prop.getProperty("db.jdbcConnection"));
        ds.addDataSourceProperty("user", prop.getProperty("db.user"));
        ds.addDataSourceProperty("password", prop.getProperty("db.password"));
	}
	
	public SchemaManager getSchemaManager() {
		return new SchemaManager(this);
	}

	public AbstractDbPlatform getPlatform() {
		return new H2Platform();
	}
}
