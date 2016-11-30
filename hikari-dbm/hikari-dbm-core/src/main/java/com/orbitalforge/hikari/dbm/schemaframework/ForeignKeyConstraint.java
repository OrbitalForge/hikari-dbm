package com.orbitalforge.hikari.dbm.schemaframework;

/*
 * Copyright (C) 2016 Travis Sharp <furiousscissors@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
