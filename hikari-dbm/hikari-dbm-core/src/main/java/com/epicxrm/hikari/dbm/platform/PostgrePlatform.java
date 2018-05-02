package com.epicxrm.hikari.dbm.platform;

import java.io.IOException;
import java.io.Writer;
import java.sql.Types;

import com.epicxrm.hikari.dbm.exception.DbTypeNotMappedException;
import com.epicxrm.hikari.dbm.schemaframework.ColumnDefinition;

import bsh.org.objectweb.asm.Type;

public class PostgrePlatform extends AbstractDbPlatform {

	@Override
	protected void setup() {
		setupDataTypes();
	}
	
	private void setupDataTypes() {
		// unregisterAllColumnTypes();
		
		registerColumnType( Types.BIT, "bool" );
		registerColumnType( Types.BIGINT, "int8" );
		registerColumnType( Types.SMALLINT, "int2" );
		registerColumnType( Types.TINYINT, "int2" );
		registerColumnType( Types.INTEGER, "int4" );
		registerColumnType( Types.CHAR, "char(1)" );
		registerColumnType( Types.VARCHAR, "varchar($l)" );
		registerColumnType( Types.FLOAT, "float4" );
		registerColumnType( Types.DOUBLE, "float8" );
		registerColumnType( Types.DATE, "date" );
		registerColumnType( Types.TIME, "time" );
		registerColumnType( Types.TIMESTAMP, "timestamp" );
		registerColumnType( Types.VARBINARY, "bytea" );
		registerColumnType( Types.BINARY, "bytea" );
		registerColumnType( Types.LONGVARCHAR, "text" );
		registerColumnType( Types.LONGVARBINARY, "bytea" );
		registerColumnType( Types.CLOB, "text" );
		registerColumnType( Types.BLOB, "oid" );
		registerColumnType( Types.NUMERIC, "numeric($p, $s)" );
		registerColumnType( Types.OTHER, "uuid" );
	}

	@Override
	protected boolean shouldEscape(String identifier) {
		// TODO: Detect keywords
		return false;
	}
	
	@Override
	protected boolean supportsDefaultConstraint() {
		return true;
	}

	@Override
	public String writeCreateSchema(String schema) {
		return String.format("CREATE SCHEMA IF NOT EXISTS %s", this.escapeIdentifier(schema));
	}
	
	private String writeCreateSequence(ColumnDefinition column, Writer writer) throws DbTypeNotMappedException, IOException {
		String sequenceName = joinIdentifiers(column.getSchemaName(), String.format("seq_%s_%s", column.getTableName(), column.getColumnName()));
		writer.write(String.format("CREATE SEQUENCE IF NOT EXISTS %s;", sequenceName));
		return sequenceName;
	}
	
	@Override
	public void writeAutoIncrement(ColumnDefinition column, Writer writer) throws DbTypeNotMappedException, IOException {
		String sequenceName = writeCreateSequence(column, writer);
    	writeAlterColumn(column.getSchemaName(), column.getTableName(), column.getColumnName(), "ALTER COLUMN", writer);
    	writer.write(String.format("SET DEFAULT nextval('%s'::regclass);", sequenceName));
	}

}
