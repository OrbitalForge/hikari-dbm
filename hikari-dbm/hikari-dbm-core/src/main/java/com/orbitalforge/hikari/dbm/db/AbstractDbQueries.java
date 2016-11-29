package com.orbitalforge.hikari.dbm.db;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class AbstractDbQueries {
	public static final String GET_TABLES;
	public static final String GET_TABLE_SINGLE;
	public static final String GET_TABLE_COLUMNS;

	static{
		GET_TABLES = loadResourceToString("queries/tableQuery.sql");
		GET_TABLE_SINGLE = loadResourceToString("queries/tableQuery_single.sql");
		GET_TABLE_COLUMNS = loadResourceToString("queries/tableColumnsQuery.sql");
	}
	
	@SuppressWarnings("deprecation")
	public static String loadResourceToString(final String path){
	    final InputStream stream =
	        Thread
			    .currentThread()
			    .getContextClassLoader()
			    .getResourceAsStream(path);
	    try{
	        return IOUtils.toString(stream);
	    } catch(final IOException e){
	        throw new IllegalStateException(e);
	    } finally{
	        IOUtils.closeQuietly(stream);
	    }
	}
}
