package com.orbitalforge.hikari.dbm.schemaframework;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;

import com.orbitalforge.hikari.dbm.db.Helpers;
import com.orbitalforge.hikari.dbm.exception.HikariDbmException;
import com.orbitalforge.hikari.dbm.platform.AbstractDbPlatform;

public class TableDefinition extends DatabaseObjectDefinition {
	private final Map<String, ColumnDefinition> columns = new LinkedHashMap<String, ColumnDefinition>();
	private final Map<String, ForeignKeyConstraint> foriegnKeys = new LinkedHashMap<String, ForeignKeyConstraint>();
	
	/* Constructors */
	public TableDefinition(Map<String, Object> data) {
		if(data.containsKey("table_name")) setName((String)data.get("table_name"));
		if(data.containsKey("table_schema")) setSchema((String)data.get("table_schema"));
	}
	
	public TableDefinition() {
		super();
	}
	/* ------------- */
	
	/* Properties */
	public String getSchema() { return getProperty("schema", ""); }
	public void setSchema(String value) { setProperty("schema", value); }
	
    /* Column Methods */
    public TableDefinition removeColumn(String column) {
		// TODO: Track changes and persist to database.
		columns.remove(column);
		return this;
	}
	
	public TableDefinition addColumn(ColumnDefinition column) {
		if(columns.containsKey(column.getName())) {
			throw new RuntimeException("Column Exists");
		}
		
		columns.put(column.getName(), column);
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
	
	/* Foriegn Key Methods */
	public void addForeignKeyConstraint(ForeignKeyConstraint constraint) {
		if(foriegnKeys.containsKey(constraint.getName())) {
			throw new RuntimeException("Foriegn Key Exists");
		}
		
		foriegnKeys.put(constraint.getName(), constraint);
    }
	
    public void addForeignKeyConstraint(String name, String sourceField, String targetField, String targetTable) {
        ForeignKeyConstraint foreignKey = new ForeignKeyConstraint(name, sourceField, targetField, targetTable);
        addForeignKeyConstraint(foreignKey);
    }
}
