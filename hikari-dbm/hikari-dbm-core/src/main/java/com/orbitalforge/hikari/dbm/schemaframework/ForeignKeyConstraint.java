package com.orbitalforge.hikari.dbm.schemaframework;

import java.io.IOException;
import java.io.Writer;

import com.orbitalforge.hikari.dbm.exception.HikariDbmException;
import com.orbitalforge.hikari.dbm.platform.AbstractDbPlatform;

public class ForeignKeyConstraint extends Constraint {
	public ForeignKeyConstraint(String name, String sourceField, String targetTable, String targetField) {
		setName(name);
		setSourceField(sourceField);
		setTargetTable(targetTable);
		setTargetField(targetField);
	}
	
	public String getSourceField() { return getProperty("sourceField", ""); }
	public void setSourceField(String value) { setProperty("sourceField", value); }
	
	public String getTargetTable() { return getProperty("targetTable", ""); }
	public void setTargetTable(String value) { setProperty("targetTable", value); }
	
	public String getTargetField() { return getProperty("targetField", ""); }
	public void setTargetField(String value) { setProperty("targetField", value); }
}
