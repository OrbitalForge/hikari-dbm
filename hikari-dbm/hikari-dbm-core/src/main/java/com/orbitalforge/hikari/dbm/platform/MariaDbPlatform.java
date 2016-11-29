package com.orbitalforge.hikari.dbm.platform;

import java.io.IOException;
import java.io.Writer;
import java.sql.Types;

import com.orbitalforge.hikari.dbm.schemaframework.ColumnDefinition;

public class MariaDbPlatform extends AbstractDbPlatform{
	@Override
	public void setup() {
		identifierFormat = "%s";	// MariaDb does not support double quoted escaped identifiers
		// NVARCHAR = LONGTEXT
		registerColumnType(Types.NVARCHAR, "LONGTEXT");
	}

	@Override
	public void buildAutoIncrement(ColumnDefinition columnDefinition, Writer writer) throws IOException {
		writer.write(" AUTO_INCREMENT");
	}
}
