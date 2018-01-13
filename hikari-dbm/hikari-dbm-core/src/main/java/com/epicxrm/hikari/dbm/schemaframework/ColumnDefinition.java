package com.epicxrm.hikari.dbm.schemaframework;

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

import java.util.Map;

import com.epicxrm.hikari.dbm.db.Helpers;

/*
 	TABLE_CATALOG,
	TABLE_SCHEMA,
	TABLE_NAME,
	COLUMN_NAME,
	ORDINAL_POSITION,
	IS_NULLABLE,
	DATA_TYPE,
	CHARACTER_MAXIMUM_LENGTH,
	NUMERIC_PRECISION,
	NUMERIC_SCALE
 */

public class ColumnDefinition extends TableObjectDefinition {
	public static final String PROPERTY_COLUMN_NAME = "column_name";
	
	public ColumnDefinition(Map<String, Object> data) {
		super();
		properties = data;
	}
	
	public ColumnDefinition() {
		super();
	}
	
	public String getColumnName() { return getProperty(PROPERTY_COLUMN_NAME); }
	public void setColumnName(String value) { setProperty(PROPERTY_COLUMN_NAME, value); }
	
	public boolean getIsAutoIncrement() { return getProperty("isAutoIncrement", false); }
	public void setIsAutoIncrement(boolean value) { setProperty("isAutoIncrement", value); }
	
	public boolean getIsNullable() { return getProperty("isNullable", true); }
	public void setIsNullable(boolean value) { setProperty("isNullable", value); }
	
	public int getDbType() { return getProperty("dbType", Integer.MIN_VALUE); }
	public void setDbType(int type) { setProperty("dbType", type); }
	
	public long getLength() { return getProperty("length", 0L); }
	public void setLength(long value) { setProperty("length", value); }
	
	public int getPrecision() { return getProperty("precision", 18); }
	public void setPrecision(int value) { setProperty("precision", value); }
	
	public int getScale() { return getProperty("scale", 4); }
	public void setScale(int value) { setProperty("scale", value); }
	
	public Object getDefaultValue() { return getProperty("defaultValue"); }
	public void setDefaultValue(Object value) { setProperty("defaultValue", value); }
}
