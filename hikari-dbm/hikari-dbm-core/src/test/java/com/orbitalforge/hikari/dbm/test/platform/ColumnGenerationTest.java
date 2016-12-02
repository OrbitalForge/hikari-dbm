package com.orbitalforge.hikari.dbm.test.platform;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.sql.Types;
import java.util.Map;
import java.util.TreeMap;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orbitalforge.hikari.dbm.exception.DbTypeNotMappedException;
import com.orbitalforge.hikari.dbm.exception.HikariDbmException;
import com.orbitalforge.hikari.dbm.exception.MissingParameterException;
import com.orbitalforge.hikari.dbm.schemaframework.ColumnDefinition;

public class ColumnGenerationTest extends GeneratorTest {
	@Test
	public void test_baseColumnGenerationExceptions() throws HikariDbmException, IOException {
		Writer writer = new StringWriter();
		ColumnDefinition column = new ColumnDefinition();
		column.setName("TEST");
		column.setDbType(Types.NVARCHAR);
		
		platform.writeColumn(column, writer);
		Assert.assertEquals(writer.toString(), Constants.BASIC_COLUMN);
	}
	
	private void test_dbTypeNotMapped(int type, String typeName) throws IOException {
		ColumnDefinition column = new ColumnDefinition();
		column.setDbType(type);
		boolean exception_thrown = false;
		try { platform.writeDbType(column, new StringWriter()).toString(); } 
		catch (DbTypeNotMappedException e) { exception_thrown = true; }
		Assert.assertEquals(exception_thrown, true, "Expected a DbTypeNotMappedException for DbType `" + typeName + "`");
	}
	
	private void test_dbTypeValueAssertion(ColumnDefinition column, String exptected) throws DbTypeNotMappedException, IOException {
		Assert.assertEquals(platform.writeDbType(column, new StringWriter()).toString(), exptected);
	}
	
	private void test_dbTypeValue(int type, String expected) throws IOException, DbTypeNotMappedException {
		ColumnDefinition column = new ColumnDefinition();
		column.setDbType(type);
		test_dbTypeValueAssertion(column, expected);
	}
	
	private void test_dbTypeValue(int type, long length, String expected) throws IOException, DbTypeNotMappedException {
		ColumnDefinition column = new ColumnDefinition();
		column.setDbType(type);
		column.setLength(length);
		test_dbTypeValueAssertion(column, expected);
	}
	
	private void test_dbTypeValue(int type, int precision, int scale, String expected) throws IOException, DbTypeNotMappedException {
		ColumnDefinition column = new ColumnDefinition();
		column.setDbType(type);
		column.setPrecision(precision);
		column.setScale(scale);
		test_dbTypeValueAssertion(column, expected);
	}
	
	private void test_dbTypeVariableLength(int type, String expected) throws DbTypeNotMappedException, IOException {
		test_dbTypeValue(type, expected + "(MAX)");
		test_dbTypeValue(type, 500, expected + "(500)");
		test_dbTypeValue(type, Long.MAX_VALUE, expected + "(9223372036854775807)");
		test_dbTypeValue(type, Long.MIN_VALUE, expected + "(MAX)");
	}
	
	private void test_dbTypeVariablePrecision(int type, String expected, boolean hasScale) throws DbTypeNotMappedException, IOException {
		if(hasScale) {
			test_dbTypeValue(type, 19, 4, expected + "(19,4)");
			test_dbTypeValue(type, 8, 8, expected + "(8,8)");
			test_dbTypeValue(type, 0, 0, expected + "(0,0)");
			test_dbTypeValue(type, -1, -1, expected + "(-1,-1)");
		} else {
			test_dbTypeValue(type, 19, 0, expected + "(19)");
			test_dbTypeValue(type, 8, 0, expected + "(8)");
			test_dbTypeValue(type, 0, 0, expected + "(0)");
			test_dbTypeValue(type, -1, 0, expected + "(-1)");
		}
	}
	
	@Test
	public void test_dbTypeNotMapped() throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = Types.class.getDeclaredFields();
		Map<Integer, String> types = new TreeMap<Integer, String>();

		for(Field field : fields) {
			if(!platform.isTypeMapped(field.getInt(null))) {
				types.put(field.getInt(null), field.getName());
			}
		}
		
		for(int key : types.keySet()) {
			try {
				test_dbTypeNotMapped(key, types.get(key));
				System.out.println(String.format("test_dbTypeNotMapped(): %s is not mapped.", types.get(key)));
			} catch (IOException e) {
				System.out.println(String.format("%s is mapped.", types.get(key)));
			}
		}
	}
	
	@Test
	public void test_dbTypeVariableLength() throws HikariDbmException, IOException {
		test_dbTypeVariableLength(Types.VARBINARY, "bit varying");
		test_dbTypeVariableLength(Types.LONGVARBINARY, "bit varying");
		test_dbTypeVariableLength(Types.CHAR, "char");
		test_dbTypeVariableLength(Types.VARCHAR, "varchar");
		test_dbTypeVariableLength(Types.LONGVARCHAR, "varchar");
		test_dbTypeVariableLength(Types.NCHAR, "nchar");
		test_dbTypeVariableLength(Types.NVARCHAR, "nvarchar");
		test_dbTypeVariableLength(Types.LONGNVARCHAR, "nvarchar");
	}
	
	@Test
	public void test_dbTypeVariablePrecision() throws HikariDbmException, IOException {
		test_dbTypeVariablePrecision(Types.DECIMAL, "decimal", true);
		test_dbTypeVariablePrecision(Types.NUMERIC, "numeric", true);
		test_dbTypeVariablePrecision(Types.FLOAT, "float", false);
	}
	
	@Test
	public void test_dbTypeStandard() throws DbTypeNotMappedException, IOException {
		test_dbTypeValue(Types.BIT, "bit");
		test_dbTypeValue(Types.BOOLEAN, "boolean");
		test_dbTypeValue(Types.TINYINT, "tinyint");
		test_dbTypeValue(Types.SMALLINT, "smallint");
		test_dbTypeValue(Types.INTEGER, "integer");
		test_dbTypeValue(Types.BIGINT, "bigint");
		test_dbTypeValue(Types.REAL, "real");
		test_dbTypeValue(Types.DATE, "date");
		test_dbTypeValue(Types.TIME, "time");
		test_dbTypeValue(Types.TIMESTAMP, "timestamp");
		test_dbTypeValue(Types.BLOB, "blob");
		test_dbTypeValue(Types.CLOB, "clob");
		test_dbTypeValue(Types.NCLOB, "nclob");
	}
	
	@Test
	public void test_columnDefaultValue() throws HikariDbmException, IOException {
		platform.setIdentifierFormat("%s");
		ColumnDefinition column = new ColumnDefinition();
		column.setName("TEST");
		column.setDbType(Types.NVARCHAR);
		
		boolean exception_thrown = false;
		try { platform.writeDefault(column, new StringWriter()).toString(); } 
		catch (MissingParameterException e) { exception_thrown = true; }
		Assert.assertEquals(exception_thrown, true, "Expected a MissingParameterException");
		
		column.setTable("sTable");
		column.setSchema("sSchema");
		
		Assert.assertEquals(platform.writeDefault(column, new StringWriter()).toString(), Constants.COLUMN_NULL_DEFAULT);
		
		column.setDefaultValue("'String Value'");
		Assert.assertEquals(platform.writeDefault(column, new StringWriter()).toString(), Constants.COLUMN_BASIC_DEFAULT);
		
		column.setDefaultValue(15.0);
		Assert.assertEquals(platform.writeDefault(column, new StringWriter()).toString(), Constants.COLUMN_NUMERIC_DEFAULT);
		
		column.setDefaultValue("TIMESTAMP");
		Assert.assertEquals(platform.writeDefault(column, new StringWriter()).toString(), Constants.COLUMN_FUNC_DEFAULT);
	}
}
