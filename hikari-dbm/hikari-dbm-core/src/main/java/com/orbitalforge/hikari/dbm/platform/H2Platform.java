package com.orbitalforge.hikari.dbm.platform;

import java.io.IOException;
import java.io.Writer;

import com.orbitalforge.hikari.dbm.schemaframework.ColumnDefinition;

public class H2Platform extends AbstractDbPlatform {
	@Override
	public void setup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildAutoIncrement(ColumnDefinition columnDefinition, Writer writer)
			throws IOException {
		writer.write(" AUTO_INCREMENT");
	}
}
