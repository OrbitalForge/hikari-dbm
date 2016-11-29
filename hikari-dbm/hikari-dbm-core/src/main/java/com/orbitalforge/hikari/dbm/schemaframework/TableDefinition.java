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
	
	public TableDefinition(Map<String, Object> data) {
		if(data.containsKey("table_name")) setName((String)data.get("table_name"));
		if(data.containsKey("table_schema")) setSchema((String)data.get("table_schema"));
	}
	
	public TableDefinition() {
		super();
	}
	
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

	@Override
	public Writer buildCreationWriter(AbstractDbPlatform platform, Writer writer) throws HikariDbmException  {
		// TODO Auto-generated method stub
		try {
			writer.write("CREATE TABLE ");
			// TODO: Add schema prefix ...
			writer.write(platform.joinIdentifiers(this.getName()));
			writer.write(" ( ");
			
			ColumnDefinition[] columnDefs = new ColumnDefinition[this.columns.size()];
			columns.values().toArray(columnDefs);
			String[] columnValues = new String[columnDefs.length];
			
			for(int i = 0; i < columnDefs.length; i++) {
				columnValues[i] = columnDefs[i].buildCreationWriter(platform, new StringWriter()).toString();
			}
			
			writer.write(Helpers.join(", ", columnValues));
			
			writer.write(" );");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return writer;
	}

	@Override
	public Writer buildDeletionWriter(AbstractDbPlatform platform, Writer writer) {
		// TODO Auto-generated method stub
		return writer;
	}
	
	public String getSchema() { return getProperty("schema", ""); }
	public void setSchema(String value) { setProperty("schema", value); }
}
