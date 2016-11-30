package com.orbitalforge.hikari.dbm.test.platform;

import java.io.IOException;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orbitalforge.hikari.dbm.exception.HikariDbmException;
import com.orbitalforge.hikari.dbm.exception.MissingParameterException;
import com.orbitalforge.hikari.dbm.schemaframework.ForeignKeyConstraint;

import junit.framework.Assert;
import junit.framework.TestCase;

public class GenericSqlGenerationTests extends TestCase {
	private GenericPlatform platform;
	private static final Logger logger = 
	        LoggerFactory.getLogger(GenericSqlGenerationTests.class);
	
	protected void setUp() {
		platform = new GenericPlatform();
		platform.setIdentifierFormat("\"%s\"");
	}

	public void test_identifierEscape() {
		platform.setIdentifierFormat("\"%s\"");
		
		Assert.assertEquals("", platform.escapeIdentifier(""));
		Assert.assertEquals("", platform.escapeIdentifier(null));
		Assert.assertEquals("\"sample\"", platform.escapeIdentifier("sample"));
		Assert.assertEquals("\"sample.sample\"", platform.escapeIdentifier("sample.sample"));
		Assert.assertEquals("", platform.joinIdentifiers("", "", ""));
		Assert.assertEquals("\"sample\"", platform.joinIdentifiers("sample"));
		Assert.assertEquals("\"sample\".\"sample\"", platform.joinIdentifiers("sample", "sample"));
		Assert.assertEquals("", platform.joinIdentifiers(null));
		Assert.assertEquals("\"sample\"", platform.joinIdentifiers("sample", null));
		Assert.assertEquals("", platform.joinIdentifiers(null, null));
		Assert.assertEquals("\"sample\".\"sample\"", platform.joinIdentifiers(null, "sample", "sample", null));
	}
	
	public void test_writeForiegnKey() throws HikariDbmException, IOException {
		platform.setIdentifierFormat("%s");
		
		Exception e = null;
		ForeignKeyConstraint fk = 
				new ForeignKeyConstraint(
						"test", 
						"sField", 
						"tSchema", "tTable", "tField");
		try { platform.writeConstraint(fk, new StringWriter()).toString(); } 
		catch (MissingParameterException ex) { e = ex; }
		Assert.assertNotNull("Expected a MissingParameterException to be thrown. The source table and schema have not been set on the ForeignKeyConstraint", e);
		Assert.assertEquals(null, fk.getSchema());
		Assert.assertEquals(null, fk.getTable());
		Assert.assertEquals("sField", fk.getField());
		Assert.assertEquals("tSchema", fk.getTargetSchema());
		Assert.assertEquals("tTable", fk.getTargetTable());
		Assert.assertEquals("tField", fk.getTargetField());
		
		fk.setSchema("sSchema");
		fk.setTable("sTable");
		
		Assert.assertEquals("sSchema", fk.getSchema());
		Assert.assertEquals("sTable", fk.getTable());
		Assert.assertEquals(Constants.ALTER_TABLE_FK, platform.writeConstraint(fk, new StringWriter()).toString());
	}
	
}
