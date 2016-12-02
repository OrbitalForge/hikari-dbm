package com.orbitalforge.hikari.dbm.test.schemaframework;

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
import java.sql.SQLException;

import org.testng.annotations.Test;

import com.orbitalforge.hikari.dbm.db.AbstractDbService;
import com.orbitalforge.hikari.dbm.exception.HikariDbmException;
import com.orbitalforge.hikari.dbm.schemaframework.SchemaManager;
import com.orbitalforge.hikari.dbm.schemaframework.TableDefinition;

/**
 * These tests are more "Integration Tests" using a memory only database.
 */
public class SchemaManagerTest {
	private final AbstractDbService service;
	
	public SchemaManagerTest() {
		service = new AbstractDbService();
	}
	
	@Test
	public void test_createTable() throws HikariDbmException, IOException, SQLException {
		SchemaManager sm = service.getSchemaManager();
		sm.createTable(TableGenerationTest.createSampleTable());
		sm.getTableNames();
		
		for(String table : sm.getTableNames()) {
			System.out.println(table);
			String[] qualifier = table.split("\\.");
			
			TableDefinition def = sm.getTable(
					qualifier[0].replaceAll("\"", ""), 
					qualifier[1].replaceAll("\"", ""));
			
			System.out.println(def.getSchema());
			System.out.println(def.getName());
		}
	}
}
