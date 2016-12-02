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

import java.util.LinkedHashMap;
import java.util.Map;

public class TableDefinition extends TableObjectDefinition {
	private final Map<String, ColumnDefinition> columns = new LinkedHashMap<String, ColumnDefinition>();
	private final Map<String, Constraint> constraints = new LinkedHashMap<String, Constraint>();
	
	/* Constructors */
	public TableDefinition(Map<String, Object> data) {
		this();
		properties = data; // Sets the internal object without needing to do "additional" mapping.
	}
	
	public TableDefinition() {
		super();
	}
	/* ------------- */
		
    /* Column Methods */
    public TableDefinition removeColumn(String column) {
		// TODO: Track changes and persist to database.
		columns.remove(column);
		return this;
	}
	
	public TableDefinition addColumn(ColumnDefinition column) {
		if(columns.containsKey(column.getColumnName())) {
			throw new RuntimeException("Column Exists");
		}
		// This will populate the table name and schema
		column.setSchemaName(getSchemaName());
		column.setTableName(getTableName());
		columns.put(column.getColumnName(), column);
		return this;
	}
	
	public void addColumns(ColumnDefinition... columnDefinitions) {
		for(ColumnDefinition column : columnDefinitions) {
			addColumn(column);
		}
	}
	
	public ColumnDefinition[] getColumns() {
		ColumnDefinition[] result = new ColumnDefinition[columns.size()];
		// TODO: Implement cloning
		columns.values().toArray(result);
		
		return result;
	}
	
	public ColumnDefinition getColumn(String key) {
		return columns.get(key);
	}
	
	public void addConstraint(Constraint constraint) {
		if(constraints.containsKey(constraint.getConstraintIdentifier())) {
			throw new RuntimeException(constraint.getConstraintIdentifier() + " Exists");
		}
		
		// Naively assumes schema and table name are already set.
		constraint.setSchemaName(getSchemaName());
		constraint.setTableName(getTableName());
		constraints.put(constraint.getConstraintIdentifier(), constraint);
    }
	
	public Constraint[] getConstraints() {
		Constraint[] results = new Constraint[constraints.size()];
		constraints.values().toArray(results);
		return results;
	}
}
