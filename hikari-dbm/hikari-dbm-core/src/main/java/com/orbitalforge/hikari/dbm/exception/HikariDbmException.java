package com.orbitalforge.hikari.dbm.exception;

public class HikariDbmException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public HikariDbmException() { }
	public HikariDbmException(String message) { super(message); }
	public HikariDbmException(Throwable throwable) { super(throwable); }
	public HikariDbmException(String message, Throwable cause) { super(message, cause); }
	public HikariDbmException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) { super(message, cause, enableSuppression, writableStackTrace); }
}
