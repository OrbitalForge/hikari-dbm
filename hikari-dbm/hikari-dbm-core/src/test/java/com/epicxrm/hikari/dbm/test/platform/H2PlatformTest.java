package com.epicxrm.hikari.dbm.test.platform;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Map;
import java.util.TreeMap;

import org.testng.annotations.Test;

import com.epicxrm.hikari.dbm.db.AbstractDbService;
import com.epicxrm.hikari.dbm.exception.HikariDbmException;
import com.epicxrm.hikari.dbm.schemaframework.ColumnDefinition;
import com.epicxrm.hikari.dbm.schemaframework.TableDefinition;

public class H2PlatformTest {
	private final AbstractDbService service;
	
	public H2PlatformTest() {
		service = new AbstractDbService();
	}
	
	public static ColumnDefinition createColumn(String name, int type) {
		ColumnDefinition def = new ColumnDefinition();
		def.setColumnName(name);
		def.setDbType(type);
		return def;
	}
	
	private void dropSampleTable() throws SQLException {
		Connection c = service.getDataSource().getConnection();
		Statement s = c.createStatement();
		try {
			s.execute("DROP TABLE \"sampleTable\";");
		} catch(Exception e) { }
		finally { 
			s.close();
			c.close();
		}
	}
	
	@Test
	public void test_tableCreation() throws IllegalArgumentException, IllegalAccessException, SQLException, HikariDbmException, IOException {
		Field[] fields = Types.class.getDeclaredFields();
		Map<Integer, String> types = new TreeMap<Integer, String>();		
		for(Field field : fields) {
			if(service.getPlatform().isTypeMapped(field.getInt(null))) {
				TableDefinition table = new TableDefinition();
				table.setTableName("sampleTable");
				types.put(field.getInt(null), field.getName());
				table.addColumn(createColumn("col_" + table.getColumns().length, field.getInt(null)));
				service.getSchemaManager().createTable(table);
				dropSampleTable();
			}
		}
	}
}
