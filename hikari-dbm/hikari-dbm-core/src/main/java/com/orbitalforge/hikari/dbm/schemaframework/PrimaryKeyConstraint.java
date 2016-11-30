package com.orbitalforge.hikari.dbm.schemaframework;

public class PrimaryKeyConstraint extends UniqueConstraint {
	public static final String CONSTRAINT_TYPE = "PK";

	@Override
	public String getConstraintType() {
		return PrimaryKeyConstraint.CONSTRAINT_TYPE;
	}
}
