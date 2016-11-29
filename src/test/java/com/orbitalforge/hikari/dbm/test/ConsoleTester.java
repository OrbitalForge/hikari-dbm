package com.orbitalforge.hikari.dbm.test;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;

import com.orbitalforge.hikari.dbm.db.AbstractDbService;
import com.orbitalforge.hikari.dbm.exception.HikariDbmException;
import com.orbitalforge.hikari.dbm.schemaframework.ColumnDefinition;
import com.orbitalforge.hikari.dbm.schemaframework.SchemaManager;
import com.orbitalforge.hikari.dbm.schemaframework.TableDefinition;

public class ConsoleTester {

	public static void main(String[] args) throws HikariDbmException, IOException, SQLException {
		AbstractDbService service = new AbstractDbService();	
		SchemaManager sm = service.getSchemaManager();
		sm.createTable(createSampleTable());
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
	
	public static TableDefinition createSampleTable() {
		TableDefinition table = new TableDefinition();
		table.setName("account");
		// table.setQualifier("\"forge");
		
		ColumnDefinition def1 = new ColumnDefinition();
		def1.setName("id");
		def1.setDbType(Types.BIGINT);
		def1.setIsPrimaryKey(true);
		def1.setIsAutoIncrement(true);
		
		ColumnDefinition def2 = new ColumnDefinition();
		def2.setDbType(Types.NVARCHAR);
		def2.setName("kubo");
		
		ColumnDefinition def3 = new ColumnDefinition();
		def3.setName("monies");
		def3.setDbType(Types.DECIMAL);
		
		table.addColumn(def1);
		table.addColumn(def2);
		table.addColumn(def3);
		
		return table;
	}
}
