package com.orbitalforge.hikari.dbm;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.slf4j.Logger;

import com.orbitalforge.hikari.dbm.db.Helpers;
import com.orbitalforge.hikari.dbm.db.NamedParameterStatement;

public final class InternalLogger {

	public static void logSqlStatement(Logger log, NamedParameterStatement nps) {
		if(log.isTraceEnabled()) {
			PreparedStatement statement = nps.getStatement();
			log.trace(Helpers.EOL + statement.toString());
		}
	}
	
	public static void logSqlStatement(Logger log, PreparedStatement statement) {
		if(log.isTraceEnabled()) {
			log.trace(Helpers.EOL + statement.toString());
		}
	}

	public static void logSqlStatement(Logger log, Statement statement) {
		if(log.isTraceEnabled()) {
			log.trace(Helpers.EOL + statement.toString());
		}
	}
}
