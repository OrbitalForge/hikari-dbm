package com.orbitalforge.hikari.dbm.test;

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
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orbitalforge.hikari.dbm.db.AbstractDbService;
import com.orbitalforge.hikari.dbm.exception.HikariDbmException;
import com.orbitalforge.hikari.dbm.schemaframework.ColumnDefinition;
import com.orbitalforge.hikari.dbm.schemaframework.PrimaryKeyConstraint;
import com.orbitalforge.hikari.dbm.schemaframework.SchemaManager;
import com.orbitalforge.hikari.dbm.schemaframework.TableDefinition;
import com.orbitalforge.hikari.dbm.schemaframework.UniqueConstraint;

public class ConsoleTester {
	public static final Logger LOG = LoggerFactory.getLogger(ConsoleTester.class);
	
	public static void main(String[] args) throws HikariDbmException, IOException, SQLException {
		LOG.debug("ENTER MAIN");
		AbstractDbService service = new AbstractDbService();	
		SchemaManager sm = service.getSchemaManager();
		sm.createTable(createSampleTable());
		sm.getTableNames();
		
		for(String table : sm.getTableNames()) {
			String[] qualifier = table.split("\\.");
			TableDefinition def = sm.getTable(
					qualifier[0].replaceAll("\"", ""), 
					qualifier[1].replaceAll("\"", ""));
		}
	}
	
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
}
