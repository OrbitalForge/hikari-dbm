package com.orbitalforge.hikari.dbm.schemaframework;

public abstract class Constraint extends DatabaseObjectDefinition {
	public abstract String getConstraintType();
	
	public String getSchema() { return getProperty("schema", ""); }
	public void setSchema(String value) { setProperty("schema", value); }
	
	public String getTable() { return getProperty("table"); }
	public void setTable(String value) { setProperty("table", value); }
	
	public String getConstraintIdentifier() {
		return String.format("%s_%s", getConstraintType(), getName()).toUpperCase();
	}
}
