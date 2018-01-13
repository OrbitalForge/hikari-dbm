package com.epicxrm.hikari.dbm.schemaframework;

public abstract class TableObjectDefinition extends DatabaseObjectDefinition {
	public static final String PROPERTY_SCHEMA = "table_schema";
	public static final String PROPERTY_TABLE_NAME = "table_name";
	
	/* Properties */
	public String getSchemaName() { return getProperty(PROPERTY_SCHEMA, ""); }
	public void setSchemaName(String value) { setProperty(PROPERTY_SCHEMA, value); }
	
	public String getTableName() { return getProperty(PROPERTY_TABLE_NAME); }
	public void setTableName(String value) { setProperty(PROPERTY_TABLE_NAME, value); }
}
