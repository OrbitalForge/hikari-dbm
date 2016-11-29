package com.orbitalforge.hikari.dbm.schemaframework;

public class PrimaryKeyConstraint extends Constraint {
	public static final String CONSTRAINT_TYPE = "PK";

	@Override
	public String getConstraintType() {
		return CONSTRAINT_TYPE;
	}
	
	public String[] getFields() { return getProperty("fields", new String[]{}); }
	public void setFields(String... fields) { setProperty("fields", fields); }
}
