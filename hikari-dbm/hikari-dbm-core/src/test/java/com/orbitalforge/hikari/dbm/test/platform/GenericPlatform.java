package com.orbitalforge.hikari.dbm.test.platform;

import java.io.IOException;
import java.io.Writer;

import com.orbitalforge.hikari.dbm.platform.AbstractDbPlatform;
import com.orbitalforge.hikari.dbm.schemaframework.ColumnDefinition;

public class GenericPlatform extends AbstractDbPlatform {
	@Override
	public void setup() {
	}
	
	public void setIdentifierFormat(String format) {
		this.identifierFormat = format;
	}

	@Override
	public void buildAutoIncrement(ColumnDefinition columnDefinition, Writer writer) throws IOException {
		writer.write(" AUTO_INCREMENT");
	}
}
