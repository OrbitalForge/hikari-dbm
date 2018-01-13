package com.epicxrm.hikari.dbm;

import java.sql.PreparedStatement;
import org.slf4j.Logger;

import com.epicxrm.hikari.dbm.db.Helpers;
import com.epicxrm.hikari.dbm.db.NamedParameterStatement;

public final class InternalLogger {

	public static void logSqlStatement(Logger log, NamedParameterStatement nps) {
		if(log.isTraceEnabled()) {
			PreparedStatement statement = nps.getStatement();
			log.trace(Helpers.EOL + statement.toString());
		}
	}

	public static void logSqlStatement(Logger log, Object object) {
		if(log.isTraceEnabled()) {
			log.trace(Helpers.EOL + object.toString());
		}
	}
}
