package com.orbitalforge.hikari.dbm.exception;

public class UnknownConstraintException extends HikariDbmException {
	private static final long serialVersionUID = 1L;

	public UnknownConstraintException() { }
	public UnknownConstraintException(String message) { super(message); }
	public UnknownConstraintException(Throwable throwable) { super(throwable); }
	public UnknownConstraintException(String message, Throwable cause) { super(message, cause); }
	public UnknownConstraintException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) { super(message, cause, enableSuppression, writableStackTrace); }
}
