package com.orbitalforge.hikari.dbm.exception;

import java.sql.Types;

public class DbTypeNotMappedException extends HikariDbmException {
	private static final long serialVersionUID = 1L;
	
	public DbTypeNotMappedException() { }
	
	public DbTypeNotMappedException(Types type) {
		super(type.toString());
	}
}
