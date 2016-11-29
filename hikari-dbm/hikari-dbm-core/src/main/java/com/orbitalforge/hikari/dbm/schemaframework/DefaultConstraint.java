package com.orbitalforge.hikari.dbm.schemaframework;

public class DefaultConstraint extends Constraint {
	public static final String CONSTRAINT_TYPE = "DF";

	@Override
	public String getConstraintType() {
		return CONSTRAINT_TYPE;
	}
}
