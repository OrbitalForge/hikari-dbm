package com.orbitalforge.hikari.dbm.schemaframework;

public class CheckConstraint extends Constraint {
	public static final String CONSTRAINT_TYPE = "CK";

	@Override
	public String getConstraintType() {
		return CONSTRAINT_TYPE;
	}
}
