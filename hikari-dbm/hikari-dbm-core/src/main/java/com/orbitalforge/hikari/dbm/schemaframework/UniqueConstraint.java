package com.orbitalforge.hikari.dbm.schemaframework;

public class UniqueConstraint extends Constraint {
	public static final String CONSTRAINT_TYPE = "UQ";

	@Override
	public String getConstraintType() {
		return CONSTRAINT_TYPE;
	}
	
	public String[] getFields() { return getProperty("fields", new String[]{}); }
	public void setFields(String... fields) { setProperty("fields", fields); }
}
