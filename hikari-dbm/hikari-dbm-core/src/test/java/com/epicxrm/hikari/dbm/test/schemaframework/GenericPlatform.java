package com.epicxrm.hikari.dbm.test.schemaframework;

import com.epicxrm.hikari.dbm.platform.AbstractDbPlatform;

public class GenericPlatform extends AbstractDbPlatform {
	@Override
	public void setup() {
	}
	
	public void setIdentifierFormat(String format) {
		this.identifierFormat = format;
	}

	@Override
	protected boolean supportsDefaultConstraint() {
		return false;
	}

	@Override
	public String writeCreateSchema(String schema) {
		// TODO Auto-generated method stub
		return null;
	}	
}
