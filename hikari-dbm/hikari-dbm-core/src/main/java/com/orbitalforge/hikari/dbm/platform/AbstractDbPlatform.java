package com.orbitalforge.hikari.dbm.platform;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import com.orbitalforge.hikari.dbm.db.Helpers;
import com.orbitalforge.hikari.dbm.exception.DbTypeNotMappedException;
import com.orbitalforge.hikari.dbm.exception.HikariDbmException;
import com.orbitalforge.hikari.dbm.schemaframework.ColumnDefinition;
import com.orbitalforge.hikari.dbm.schemaframework.Constraint;
import com.orbitalforge.hikari.dbm.schemaframework.TableDefinition;

public abstract class AbstractDbPlatform {
	private Map<Integer, String> columnTypeMap = new HashMap<Integer, String>();
	private Map<String, Integer> reverseColumnTypeMap = new HashMap<String, Integer>();
	
	public String escapeIdentifier(String identifier) {
		String cleaned = identifier.replaceAll("\"", "").replaceAll("\'", "");
		return String.format("\"%s\"", cleaned);
	}
	
	public String joinIdentifiers(String... identifiers) {
		String[] escaped = new String[identifiers.length];
		for(int i = 0; i < identifiers.length; i++) {
			escaped[i] = escapeIdentifier(identifiers[i]);
		}
		
		return Helpers.join(".", escaped);
	}
	
	public AbstractDbPlatform() {
		this.internalSetup();
		this.setup();
	}

	/**
	 * This expects that DBTypes and their respective text can be mapped 1-1
	 * @param dbType
	 * @param type
	 */
	protected void registerColumnType(int dbType, String type) {
		this.columnTypeMap.put(dbType, type);
		// this.reverseColumnTypeMap.put(type, dbType);
	}
	
	private void internalSetup() {
		// Borrowed from 
		// https://github.com/hibernate/hibernate-orm/blob/master/hibernate-core/src/main/java/org/hibernate/dialect/Dialect.java
		registerColumnType( Types.BIT, "bit" );
		registerColumnType( Types.BOOLEAN, "boolean" );
		registerColumnType( Types.TINYINT, "tinyint" );
		registerColumnType( Types.SMALLINT, "smallint" );
		registerColumnType( Types.INTEGER, "integer" );
		registerColumnType( Types.BIGINT, "bigint" );
		registerColumnType( Types.FLOAT, "float($p)" );
		registerColumnType( Types.DECIMAL, "decimal(%p,%s)");
		registerColumnType( Types.DOUBLE, "double precision" );
		registerColumnType( Types.NUMERIC, "numeric(%p,%s)" );
		registerColumnType( Types.REAL, "real" );

		registerColumnType( Types.DATE, "date" );
		registerColumnType( Types.TIME, "time" );
		registerColumnType( Types.TIMESTAMP, "timestamp" );

		registerColumnType( Types.VARBINARY, "bit varying(%l)" );
		registerColumnType( Types.LONGVARBINARY, "bit varying(%l)" );
		registerColumnType( Types.BLOB, "blob" );

		registerColumnType( Types.CHAR, "char(%l)" );
		registerColumnType( Types.VARCHAR, "varchar(%l)" );
		registerColumnType( Types.LONGVARCHAR, "varchar(%l)" );
		registerColumnType( Types.CLOB, "clob" );

		registerColumnType( Types.NCHAR, "nchar(%l)" );
		registerColumnType( Types.NVARCHAR, "nvarchar(%l)" );
		registerColumnType( Types.LONGNVARCHAR, "nvarchar(%l)" );
		registerColumnType( Types.NCLOB, "nclob" );
	}
	
	protected abstract void setup();
	
	public String getColumnType(int type) {
		return this.columnTypeMap.get(type);
	}
	
	public int getColumnType(String dataType) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private String renderTemplate(String template, String param, String value) {
		return template.replaceAll(param, value);
	}
		
	protected void writeDbType(ColumnDefinition column, Writer writer) throws DbTypeNotMappedException, IOException {
		String typeTemplate = getColumnType(column.getDbType());
		if(typeTemplate == null) throw new DbTypeNotMappedException();
		
		typeTemplate = renderTemplate(typeTemplate, "%l", column.getLength() <= 0 ? "MAX" : Long.toString(column.getLength()));
		typeTemplate = renderTemplate(typeTemplate, "%p", Integer.toString(column.getPrecision()));
		typeTemplate = renderTemplate(typeTemplate, "%s", Integer.toString(column.getScale()));
		
		writer.write(" ");
		writer.write(typeTemplate);
	}

	protected abstract void buildAutoIncrement(ColumnDefinition columnDefinition, Writer writer) throws IOException;
	
	private Writer writeColumn(ColumnDefinition column, Writer writer) throws HikariDbmException, IOException {
		writer.write(this.escapeIdentifier(column.getName()));
		writeDbType(column, writer);
		if(column.getIsNullable()) writer.write(" NOT NULL");
		if(column.getIsAutoIncrement()) this.buildAutoIncrement(column, writer);
		if(column.getIsPrimaryKey()) writer.write(" PRIMARY KEY");

		return writer;
	}
	
	public Writer writeTable(TableDefinition table, Writer writer) throws HikariDbmException, IOException {
			String newLine = System.getProperty("line.separator");
			writer.write("CREATE TABLE ");
			// TODO: Add schema prefix ...
			writer.write(joinIdentifiers(table.getName()));
			writer.write(" ( ");
			writer.write(newLine);
			
			ColumnDefinition[] columnDefs = table.getColumns();
			String[] columns = new String[columnDefs.length];
			
			for(int i = 0; i < columnDefs.length; i++) {
				columns[i] = writeColumn(columnDefs[i], new StringWriter()).toString();
			}
			
			writer.write(Helpers.join(", " + newLine, columns));

			writer.write(" );");
			
			return writer;
	}
	
	public Writer writeConstraint(Constraint constraint, Writer writer) {
		
		return writer;
	}
}
