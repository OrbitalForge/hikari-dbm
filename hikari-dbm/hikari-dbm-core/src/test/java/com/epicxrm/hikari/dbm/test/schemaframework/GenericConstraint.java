package com.epicxrm.hikari.dbm.test.schemaframework;

import com.epicxrm.hikari.dbm.schemaframework.Constraint;

public class GenericConstraint extends Constraint {

	@Override
	public String getConstraintType() {
		return "GENERIC";
	}

}
