package com.orbitalforge.hikari.dbm.test.schemaframework;

import java.io.IOException;
import java.sql.SQLException;

import org.testng.annotations.Test;

import com.orbitalforge.hikari.dbm.db.AbstractDbService;
import com.orbitalforge.hikari.dbm.exception.HikariDbmException;
import com.orbitalforge.hikari.dbm.schemaframework.SchemaManager;
import com.orbitalforge.hikari.dbm.schemaframework.TableDefinition;
import com.orbitalforge.hikari.dbm.test.platform.TableGenerationTest;

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
