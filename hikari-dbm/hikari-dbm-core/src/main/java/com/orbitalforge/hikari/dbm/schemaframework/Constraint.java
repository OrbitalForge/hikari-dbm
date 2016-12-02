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

public abstract class Constraint extends TableObjectDefinition {
	public abstract String getConstraintType();
	public static final String PROPERTY_CONSTRAINT_NAME = "constraint_name";
	
	public String getConstraintName() { return getProperty(PROPERTY_CONSTRAINT_NAME, ""); }
	public void setConstraintName(String value) { setProperty(PROPERTY_CONSTRAINT_NAME, value); }
		
	private String getNamePrefix() {
		return getConstraintName().split("_")[0];
	}
	
	private boolean hasPrefix() {
		return (getNamePrefix().toLowerCase() != getConstraintName());
	}
	
	public String getConstraintIdentifier() {
		// If the constraint already has a prefix added, just return the name
		if(hasPrefix()) return getConstraintName();
		return String.format("%s_%s", getConstraintType(), getConstraintName());
	}
}
