package com.orbitalforge.hikari.dbm.db;

/*
 * Copyright (C) 2016 Travis Sharp <furiousscissors@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.io.InputStream;

public class AbstractDbQueries {
	public static final String GET_TABLES;
	public static final String GET_TABLE_SINGLE;
	public static final String GET_TABLE_COLUMNS;

	static{
		GET_TABLES = loadResourceToString("queries/tableQuery.sql");
		GET_TABLE_SINGLE = loadResourceToString("queries/tableQuery_single.sql");
		GET_TABLE_COLUMNS = loadResourceToString("queries/tableColumnsQuery.sql");
	}
	
	public static String loadResourceToString(final String path) {
	    final InputStream stream =
	        Thread
			    .currentThread()
			    .getContextClassLoader()
			    .getResourceAsStream(path);
	    
		try {
			String result = Helpers.toString(stream);
			stream.close();
			return result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
