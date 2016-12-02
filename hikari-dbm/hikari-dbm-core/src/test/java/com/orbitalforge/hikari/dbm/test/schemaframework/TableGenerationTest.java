package com.orbitalforge.hikari.dbm.test.schemaframework;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.orbitalforge.hikari.dbm.db.Helpers;
import com.orbitalforge.hikari.dbm.exception.HikariDbmException;
import com.orbitalforge.hikari.dbm.schemaframework.ColumnDefinition;
import com.orbitalforge.hikari.dbm.schemaframework.PrimaryKeyConstraint;
import com.orbitalforge.hikari.dbm.schemaframework.TableDefinition;
import com.orbitalforge.hikari.dbm.schemaframework.UniqueConstraint;

public class TableGenerationTest extends GeneratorTest {
	
	public static TableDefinition createSampleTable() {
		TableDefinition table = new TableDefinition();
		table.setTableName("account");
		// table.setQualifier("\"forge");
		
		ColumnDefinition def1 = new ColumnDefinition();
		def1.setColumnName("id");
		def1.setDbType(Types.BIGINT);
		def1.setIsAutoIncrement(true);
		def1.setIsNullable(false);
		
		ColumnDefinition def2 = new ColumnDefinition();
		def2.setDbType(Types.VARCHAR);
		def2.setColumnName("kubo");
		def2.setLength(767);
		def2.setDefaultValue("'Quality'");
		
		ColumnDefinition def3 = new ColumnDefinition();
		def3.setColumnName("monies");
		def3.setDbType(Types.DECIMAL);
		
		table.addColumn(def1);
		table.addColumn(def2);
		table.addColumn(def3);
		
		UniqueConstraint uq = new UniqueConstraint();
		uq.setConstraintName("SAMPLE");
		uq.setFields("kubo");
		table.addConstraint(uq);
		
		PrimaryKeyConstraint pk = new PrimaryKeyConstraint();
		pk.setConstraintName("sample");
		pk.setFields("id");
		
		table.addConstraint(pk);
		
		return table;
	}
	
	@Test
	public void test_standardTableGeneration() throws HikariDbmException, IOException {
		platform.setIdentifierFormat("%s");
		String[] lines = platform.writeTable(createSampleTable(), new StringWriter()).toString().split(Helpers.EOL);
		Assert.assertArrayEquals(lines, Constants.BASIC_TABLE);
	}
	
	@Test
	public void test_tableColumnTests() throws HikariDbmException, IOException {
		platform.setIdentifierFormat("%s");
		TableDefinition table = createSampleTable();
		Assert.assertNotNull(table.getColumn("monies"));
		Assert.assertNull(table.getColumn("badIdentifier"));
		
		ColumnDefinition def = new ColumnDefinition();
		def.setColumnName("monies");
		def.setDbType(Types.NVARCHAR);
		
		PrimaryKeyConstraint pk = new PrimaryKeyConstraint();
		pk.setConstraintName("sample");
		pk.setFields("id");
		
		boolean exceptionCalled = false;
		try { table.addColumn(def); } catch(RuntimeException e) { exceptionCalled = true; }
		Assert.assertTrue(exceptionCalled);
		exceptionCalled = false;
		
		try { table.addConstraint(pk); } catch(RuntimeException e) { exceptionCalled = true; }
		Assert.assertTrue(exceptionCalled);
		
		ColumnDefinition def1 = new ColumnDefinition();
		def1.setColumnName("monies1");
		def1.setDbType(Types.NVARCHAR);
		
		ColumnDefinition def2 = new ColumnDefinition();
		def2.setColumnName("monies2");
		def2.setDbType(Types.NVARCHAR);
		
		table.addColumns(new ColumnDefinition[] { def1, def2 });
		
		Assert.assertNotNull(table.getColumn("monies1"));
		Assert.assertNotNull(table.getColumn("monies2"));
		
		table.removeColumn("monies1");
		table.removeColumn("monies2");
		
		Assert.assertNull(table.getColumn("monies1"));
		Assert.assertNull(table.getColumn("monies2"));
	}
	
	@Test
	public void test_otherCodeCoverage() {
		platform.setIdentifierFormat("%s");
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("table_name", "_table_name");
		data.put("table_schema", "_table_schema");
		
		TableDefinition table = new TableDefinition(data);
		Assert.assertEquals(table.getTableName(), "_table_name");
		Assert.assertEquals(table.getSchemaName(), "_table_schema");
		
		table.setSchemaName("test_value");
		Assert.assertEquals(table.getSchemaName(), "test_value");
		
		data.clear();
		table = new TableDefinition(data);
		Assert.assertNull(table.getTableName());
		// Schema is not always present so it should not impact the downstream identifier joins.
		Assert.assertEquals(table.getSchemaName(), "");
	}
}
