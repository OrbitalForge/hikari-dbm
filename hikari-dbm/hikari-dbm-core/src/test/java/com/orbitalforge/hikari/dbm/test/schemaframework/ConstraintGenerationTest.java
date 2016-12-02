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

import org.testng.annotations.Test;
import org.testng.Assert;
import java.io.StringWriter;

import com.orbitalforge.hikari.dbm.exception.MissingParameterException;
import com.orbitalforge.hikari.dbm.exception.UnknownConstraintException;
import com.orbitalforge.hikari.dbm.schemaframework.ForeignKeyConstraint;
import com.orbitalforge.hikari.dbm.schemaframework.PrimaryKeyConstraint;
import com.orbitalforge.hikari.dbm.schemaframework.UniqueConstraint;

public class ConstraintGenerationTest extends GeneratorTest {
	
	@Test
	public void test_genericConstraint() throws Exception {
		GenericConstraint g = new GenericConstraint();
		try { 
			platform.writeConstraint(g, new StringWriter()).toString();
			throw new Exception("Was expecting an exception because table and name are not set.");
		} 
		catch (MissingParameterException ex) {}	
		
		g.setName("unique");
		
		try { 
			platform.writeConstraint(g, new StringWriter()).toString();
			throw new Exception("Was expecting an exception because table is not set.");
		} 
		catch (MissingParameterException ex) {}	
		
		Assert.assertEquals(null, g.getTable());
		g.setTable("sTable");
		Assert.assertEquals("sTable", g.getTable());
		
		Assert.assertEquals("", g.getSchema());
		try { platform.writeConstraint(g, new StringWriter()); }
		catch(UnknownConstraintException e ) { }
		
		Assert.assertEquals("", g.getSchema());
		g.setSchema("sSchema");		
		Assert.assertEquals("sSchema", g.getSchema());
	}
	
	@Test
	public void test_genericConstraintPrefix() {
		GenericConstraint g = new GenericConstraint();
		g.setName("test0");
		Assert.assertEquals(g.getConstraintIdentifier(), "GENERIC_test0");
		g.setName("generic_test1");
		Assert.assertEquals(g.getConstraintIdentifier(), "generic_test1");
		
		g.setName("gEnErIc_test2");
		Assert.assertEquals(g.getConstraintIdentifier(), "gEnErIc_test2");
		
		g.setName("gen_prefix_test3");
		Assert.assertEquals(g.getConstraintIdentifier(), "gen_prefix_test3");
	}
	
	@Test
	public void test_writeForeignKeyConstraint() throws Exception {
		platform.setIdentifierFormat("%s");
		ForeignKeyConstraint fk = 
				new ForeignKeyConstraint(
						"fkName", 
						"sField", 
						"tSchema", "tTable", "tField");
		
		Assert.assertEquals("sField", fk.getField());
		Assert.assertEquals("tSchema", fk.getTargetSchema());
		Assert.assertEquals("tTable", fk.getTargetTable());
		Assert.assertEquals("tField", fk.getTargetField());
		
		fk.setSchema("sSchema");
		fk.setTable("sTable");
		
		Assert.assertEquals(Constants.ALTER_TABLE_FK, platform.writeConstraint(fk, new StringWriter()).toString());
	}
	
	@Test
	public void test_writeUniqueConstraint() throws Exception {
		platform.setIdentifierFormat("%s");
		UniqueConstraint uq = new UniqueConstraint();
		uq.setName("uqName");
		uq.setSchema("sSchema");
		uq.setTable("sTable");
		
		try { platform.writeConstraint(uq, new StringWriter()); }
		catch(MissingParameterException e ) { }
		uq.setFields("sField");
		
		Assert.assertEquals(Constants.ALTER_TABLE_UQ, platform.writeConstraint(uq, new StringWriter()).toString());
	}
	
	@Test
	public void test_writePrimaryKeyConstraint() throws Exception {
		platform.setIdentifierFormat("%s");
		PrimaryKeyConstraint pk = new PrimaryKeyConstraint();
		pk.setName("pkName");
		pk.setSchema("sSchema");
		pk.setTable("sTable");
		
		try { platform.writeConstraint(pk, new StringWriter()); }
		catch(MissingParameterException e ) { }
		pk.setFields("sField");
		
		Assert.assertEquals(Constants.ALTER_TABLE_PK, platform.writeConstraint(pk, new StringWriter()).toString());
	}
}
