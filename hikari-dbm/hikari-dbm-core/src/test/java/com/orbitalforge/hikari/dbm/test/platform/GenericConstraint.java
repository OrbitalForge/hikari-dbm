package com.orbitalforge.hikari.dbm.test.platform;

import com.orbitalforge.hikari.dbm.schemaframework.Constraint;

public class GenericConstraint extends Constraint {

	@Override
	public String getConstraintType() {
		return "GENERIC";
	}

}
