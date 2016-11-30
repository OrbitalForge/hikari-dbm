package com.orbitalforge.hikari.dbm.exception;

public class MissingParameterException extends HikariDbmException {
	private static final long serialVersionUID = 1L;

	public MissingParameterException() { }
	public MissingParameterException(String message) { super(message); }
	public MissingParameterException(Throwable throwable) { super(throwable); }
	public MissingParameterException(String message, Throwable cause) { super(message, cause); }
	public MissingParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) { super(message, cause, enableSuppression, writableStackTrace); }
}
