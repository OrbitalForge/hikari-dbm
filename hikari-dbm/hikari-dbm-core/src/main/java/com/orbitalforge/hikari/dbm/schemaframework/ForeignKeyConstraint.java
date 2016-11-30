package com.orbitalforge.hikari.dbm.schemaframework;

public class ForeignKeyConstraint extends Constraint {
	public static final String CONSTRAINT_TYPE = "FK";

	@Override
	public String getConstraintType() {
		return CONSTRAINT_TYPE;
	}
	
	public ForeignKeyConstraint(
			String name, 
			String sourceField, 
			String targetSchema, String targetTable, String targetField) {
		setName(name);
		setField(sourceField);
		setTargetSchema(targetSchema);
		setTargetTable(targetTable);
		setTargetField(targetField);
	}
	
	public String getField() { return getProperty("sourceField"); }
	public void setField(String value) { setProperty("sourceField", value); }
	
	public String getTargetTable() { return getProperty("targetTable"); }
	public void setTargetTable(String value) { setProperty("targetTable", value); }
	
	public String getTargetField() { return getProperty("targetField"); }
	public void setTargetField(String value) { setProperty("targetField", value); }
	
	public String getTargetSchema() { return getProperty("targetSchema"); }
	public void setTargetSchema(String value) { setProperty("targetSchema", value); }
}
