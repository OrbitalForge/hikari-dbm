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

public abstract class Constraint extends DatabaseObjectDefinition {
	public abstract String getConstraintType();
	
	public String getSchema() { return getProperty("schema", ""); }
	public void setSchema(String value) { setProperty("schema", value); }
	
	public String getTable() { return getProperty("table"); }
	public void setTable(String value) { setProperty("table", value); }
	
	private String getNamePrefix() {
		return getName().split("_")[0];
	}
	
	private boolean hasPrefix() {
		return (getNamePrefix().toLowerCase() != getName());
	}
	
	public String getConstraintIdentifier() {
		// If the constraint already has a prefix added, just return the name
		if(hasPrefix()) return getName();
		return String.format("%s_%s", getConstraintType(), getName());
	}
}
