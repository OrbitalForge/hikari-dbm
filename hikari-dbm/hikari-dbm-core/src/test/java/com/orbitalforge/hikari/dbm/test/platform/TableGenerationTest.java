package com.orbitalforge.hikari.dbm.test.platform;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Types;

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
		table.setName("account");
		// table.setQualifier("\"forge");
		
		ColumnDefinition def1 = new ColumnDefinition();
		def1.setName("id");
		def1.setDbType(Types.BIGINT);
		def1.setIsAutoIncrement(true);
		def1.setIsNullable(false);
		
		ColumnDefinition def2 = new ColumnDefinition();
		def2.setDbType(Types.VARCHAR);
		def2.setName("kubo");
		def2.setLength(767);
		def2.setDefaultValue("'Quality'");
		
		ColumnDefinition def3 = new ColumnDefinition();
		def3.setName("monies");
		def3.setDbType(Types.DECIMAL);
		
		table.addColumn(def1);
		table.addColumn(def2);
		table.addColumn(def3);
		
		UniqueConstraint uq = new UniqueConstraint();
		uq.setName("SAMPLE");
		uq.setFields("kubo");
		table.addConstraint(uq);
		
		PrimaryKeyConstraint pk = new PrimaryKeyConstraint();
		pk.setName("sample");
		pk.setFields("id");
		
		table.addConstraint(pk);
		
		return table;
	}
	
	@Test
	public void test_standardTableGeneration() throws HikariDbmException, IOException {
		platform.setIdentifierFormat("%s");
		String[] lines = platform.writeTable(createSampleTable(), new StringWriter()).toString().split(Helpers.EOL);
		Assert.assertArrayEquals(Constants.BASIC_TABLE, lines);
	}
}
