package com.orbitalforge.hikari.dbm.exception;

public class DbTypeNotMappedException extends HikariDbmException {
	private static final long serialVersionUID = 1L;
	
	public DbTypeNotMappedException() { }
	public DbTypeNotMappedException(String message) { super(message); }
	public DbTypeNotMappedException(Throwable throwable) { super(throwable); }
	public DbTypeNotMappedException(String message, Throwable cause) { super(message, cause); }
	public DbTypeNotMappedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) { super(message, cause, enableSuppression, writableStackTrace); }
}
