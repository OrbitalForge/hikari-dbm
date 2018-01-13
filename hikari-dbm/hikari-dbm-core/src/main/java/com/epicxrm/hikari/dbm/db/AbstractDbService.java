package com.epicxrm.hikari.dbm.db;

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
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epicxrm.hikari.dbm.platform.AbstractDbPlatform;
import com.epicxrm.hikari.dbm.schemaframework.SchemaManager;
import com.zaxxer.hikari.HikariConfig;
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
	public static final Logger LOG = LoggerFactory.getLogger(AbstractDbService.class);
	/**
	 * Internal Connection
	 */
	protected final HikariDataSource dataSource;
	protected final AbstractDbPlatform platform;
	protected final SchemaManager schemaManager;
	protected final Properties properties;
	private boolean logSql = false;

	public AbstractDbService() {
		properties = loadProperties("hikari.dbm.properties");
        dataSource = new HikariDataSource(getHikariCpConfig());

        /* Setup Db Platform */
		try {
			Class<?> clazz = Class.forName(getProperty("hikari.dbm.platformClass"));
			platform = (AbstractDbPlatform)clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
       
        /* Setup Schema Manager */
        schemaManager = new SchemaManager(this);
	}
	private Properties loadProperties(String file) {
		return loadResourceToProperties(file);
	}
	
	public static Properties loadResourceToProperties(final String path) {
	    final InputStream stream =
	        Thread
			    .currentThread()
			    .getContextClassLoader()
			    .getResourceAsStream(path);
	    
		try {
			Properties props = new Properties();
			props.load(stream);
			stream.close();
			return props;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private HikariConfig getHikariCpConfig() {
		Properties props = new Properties();
		
		for(Object key : properties.keySet()) {
			if(key instanceof String && 
					((String)key).startsWith("dataSource")){
				props.setProperty((String)key, (String) properties.get((String)key));
			}
		}
		
		HikariConfig config = new HikariConfig(props);
		config.setPoolName("hikari-dbm");
		return config;
	}
	
	private String getProperty(String key, String defualtValue) { return (String)this.properties.getOrDefault(key, defualtValue); }
	private String getProperty(String key) { return (String)this.properties.getProperty(key); }
	
	public DataSource getDataSource() { return dataSource; }
	public SchemaManager getSchemaManager() { return schemaManager; }
	public AbstractDbPlatform getPlatform() { return platform; }
	
	public void setLogSql(boolean value) { logSql = value; }
	public boolean getLogSql() { return logSql; }
}
