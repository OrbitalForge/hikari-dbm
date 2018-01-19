package com.epicxrm.hikari.dbm.platform;

import java.sql.Types;

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

public class H2Platform extends AbstractDbPlatform {
	@Override
	protected void setup() {
		setupDataTypes();
	}
	
	private void setupDataTypes() {
		unregisterAllColumnTypes();
		
		// registerColumnType(Types.BIT, "bit");
		registerColumnType(Types.BOOLEAN, "boolean");
		registerColumnType(Types.TINYINT, "tinyint");
		registerColumnType(Types.SMALLINT, "smallint");
		registerColumnType(Types.INTEGER, "int");
		registerColumnType(Types.BIGINT, "bigint");
		registerColumnType(Types.FLOAT, "float");
		registerColumnType(Types.DECIMAL, "decimal(%p,%s)");
		registerColumnType(Types.DOUBLE, "double precision");
		registerColumnType(Types.NUMERIC, "decimal(%p,%s)");
		registerColumnType(Types.REAL, "real");

		registerColumnType(Types.DATE, "date");
		registerColumnType(Types.TIME, "time");
		registerColumnType(Types.TIMESTAMP, "timestamp");
		// registerColumnType(Types.TIMESTAMP_WITH_TIMEZONE, "TIMESTAMP WITH TIMEZONE");

		registerColumnType(Types.VARBINARY, "varbinary(%l)");
		registerColumnType(Types.LONGVARBINARY, "longvarbinary(%l)");
		registerColumnType(Types.BLOB, "blob(%l)");

		registerColumnType(Types.CHAR, "char(%l)");
		registerColumnType(Types.VARCHAR, "varchar(%l)");
		registerColumnType(Types.LONGVARCHAR, "varchar(%l)");
		registerColumnType(Types.CLOB, "clob(%l)");

		registerColumnType(Types.NCHAR, "nchar(%l)");
		registerColumnType(Types.NVARCHAR, "nvarchar(%l)");
		registerColumnType(Types.LONGNVARCHAR, "nvarchar(%l)");
		registerColumnType(Types.NCLOB, "nclob(%l)");
	}

	@Override
	protected boolean supportsDefaultConstraint() {
		return false;
	}
	
	@Override
	public String writeCreateSchema(String schema) {
		return String.format("CREATE SCHEMA IF NOT EXISTS %s", this.escapeIdentifier(schema));
	}
}
