package com.orbitalforge.hikari.dbm.db;

/*
 * Copyright (C) 2016 Travis Sharp <furiousscissors@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
	protected final AbstractDbPlatform platform;
	protected final SchemaManager schemaManager;
	
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
        
        /* Setup Schema Manager */
        schemaManager = new SchemaManager(this);
        
        /* Setup Db Platform */
		try {
			Class<?> clazz = Class.forName(prop.getProperty("db.platformClass"));
			platform = (AbstractDbPlatform)clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
       
	}
	
	public SchemaManager getSchemaManager() {
		return schemaManager;
	}

	public AbstractDbPlatform getPlatform() {
		return platform;
	}
}
