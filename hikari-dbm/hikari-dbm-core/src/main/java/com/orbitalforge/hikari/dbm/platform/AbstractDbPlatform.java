package com.orbitalforge.hikari.dbm.platform;

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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

import com.orbitalforge.hikari.dbm.db.Helpers;
import com.orbitalforge.hikari.dbm.exception.DbTypeNotMappedException;
import com.orbitalforge.hikari.dbm.exception.HikariDbmException;
import com.orbitalforge.hikari.dbm.exception.MissingParameterException;
import com.orbitalforge.hikari.dbm.exception.UnknownConstraintException;
import com.orbitalforge.hikari.dbm.schemaframework.ColumnDefinition;
import com.orbitalforge.hikari.dbm.schemaframework.Constraint;
import com.orbitalforge.hikari.dbm.schemaframework.DefaultConstraint;
import com.orbitalforge.hikari.dbm.schemaframework.ForeignKeyConstraint;
import com.orbitalforge.hikari.dbm.schemaframework.PrimaryKeyConstraint;
import com.orbitalforge.hikari.dbm.schemaframework.TableDefinition;
import com.orbitalforge.hikari.dbm.schemaframework.UniqueConstraint;

public abstract class AbstractDbPlatform {
	private Map<Integer, String> columnTypeMap = new HashMap<Integer, String>();
	protected String identifierFormat = "\"%s\"";
	
	public String escapeIdentifier(String identifier) {
		if(identifier == null) return "";
		String cleaned = identifier.replaceAll("\"", "").replaceAll("\'", "");
		if(cleaned == "") return cleaned;
		return String.format(identifierFormat, cleaned);
	}

	public String joinIdentifiers(String... identifiers) {
		if(identifiers == null) return "";
		
		String[] escaped = new String[identifiers.length];
		for (int i = 0; i < identifiers.length; i++) {
			escaped[i] = escapeIdentifier(identifiers[i]);
		}
		
		List<String> list = new ArrayList<String>(Arrays.asList(escaped));
		list.removeAll(Arrays.asList("", null));

		return Helpers.join(".", list);
	}

	public AbstractDbPlatform() {
		this.internalSetup();
		this.setup();
	}

	/**
	 * This expects that DBTypes and their respective text can be mapped 1-1
	 * 
	 * @param dbType
	 * @param type
	 */
	protected void registerColumnType(int dbType, String type) {
		this.columnTypeMap.put(dbType, type);
	}

	protected void unregisterColumnType(int type) {
		this.columnTypeMap.remove(type);
	}
	
	/**
	 * This will clear out the column type registry. This is needed when the
	 * platform implementation overrides all types.
	 */
	protected void unregisterAllColumnTypes() {
		this.columnTypeMap.clear();
	}
	
	private void internalSetup() {
		// Borrowed from
		// https://github.com/hibernate/hibernate-orm/blob/master/hibernate-core/src/main/java/org/hibernate/dialect/Dialect.java
		registerColumnType(Types.BIT, "bit");
		registerColumnType(Types.BOOLEAN, "boolean");
		registerColumnType(Types.TINYINT, "tinyint");
		registerColumnType(Types.SMALLINT, "smallint");
		registerColumnType(Types.INTEGER, "integer");
		registerColumnType(Types.BIGINT, "bigint");
		registerColumnType(Types.FLOAT, "float($p)");
		registerColumnType(Types.DECIMAL, "decimal(%p,%s)");
		registerColumnType(Types.DOUBLE, "double precision");
		registerColumnType(Types.NUMERIC, "numeric(%p,%s)");
		registerColumnType(Types.REAL, "real");

		registerColumnType(Types.DATE, "date");
		registerColumnType(Types.TIME, "time");
		registerColumnType(Types.TIMESTAMP, "timestamp");

		registerColumnType(Types.VARBINARY, "bit varying(%l)");
		registerColumnType(Types.LONGVARBINARY, "bit varying(%l)");
		registerColumnType(Types.BLOB, "blob");

		registerColumnType(Types.CHAR, "char(%l)");
		registerColumnType(Types.VARCHAR, "varchar(%l)");
		registerColumnType(Types.LONGVARCHAR, "varchar(%l)");
		registerColumnType(Types.CLOB, "clob");

		registerColumnType(Types.NCHAR, "nchar(%l)");
		registerColumnType(Types.NVARCHAR, "nvarchar(%l)");
		registerColumnType(Types.LONGNVARCHAR, "nvarchar(%l)");
		registerColumnType(Types.NCLOB, "nclob");
	}

	protected abstract void setup();

	public String getColumnType(int type) {
		return this.columnTypeMap.get(type);
	}

	private String renderTemplate(String template, String param, String value) {
		return template.replaceAll(param, value);
	}

	protected void writeDbType(ColumnDefinition column, Writer writer) throws DbTypeNotMappedException, IOException {
		String typeTemplate = getColumnType(column.getDbType());
		if (typeTemplate == null)
			throw new DbTypeNotMappedException();

		typeTemplate = renderTemplate(typeTemplate, "%l",
				column.getLength() <= 0 ? "MAX" : Long.toString(column.getLength()));
		typeTemplate = renderTemplate(typeTemplate, "%p", Integer.toString(column.getPrecision()));
		typeTemplate = renderTemplate(typeTemplate, "%s", Integer.toString(column.getScale()));
		writer.write(typeTemplate);
	}

	private Writer writeColumn(ColumnDefinition column, Writer writer) throws HikariDbmException, IOException {
		writer.write(this.escapeIdentifier(column.getName()));
		writer.write(" ");
		writeDbType(column, writer);
		if (!column.getIsNullable()) writer.write(" NOT NULL");
		/*
		if (column.getIsAutoIncrement())
			this.buildAutoIncrement(column, writer);
		 */
		return writer;
	}

	public Writer writeTable(TableDefinition table, Writer writer) throws HikariDbmException, IOException {
		writer.write("CREATE TABLE ");
		// TODO: Add schema prefix ...
		writer.write(joinIdentifiers(table.getName()));
		writer.write(" ( ");
		writer.write(Helpers.EOL);

		ColumnDefinition[] columnDefs = table.getColumns();
		String[] columns = new String[columnDefs.length];

		List<ColumnDefinition> autoIncrements = new ArrayList<>();
		
		for (int i = 0; i < columnDefs.length; i++) {
			columns[i] = writeColumn(columnDefs[i], new StringWriter()).toString();
			if(columnDefs[i].getIsAutoIncrement()) autoIncrements.add(columnDefs[i]);
		}

		writer.write(Helpers.join(", " + Helpers.EOL, columns));

		writer.write(" );");
		writer.write(Helpers.EOL);
		writeConstraints(writer, table.getConstraints());
		writer.write(Helpers.EOL);
		writeAutoIncrements(writer, autoIncrements.iterator());
		
		return writer;
	}

	/**
	 * Writes a collection of auto increments
	 * Arrays.stream(array).iterator();
	 * @param writer
	 * @param defs
	 * @return
	 * @throws IOException
	 * @throws DbTypeNotMappedException
	 */
    private Writer writeAutoIncrements(Writer writer, Iterator<ColumnDefinition> defs) throws IOException, DbTypeNotMappedException {
    	boolean and = false;
    	while(defs.hasNext()) {
    		ColumnDefinition def = defs.next();
    		
    		if(and) {
    			writer.write(Helpers.EOL);
    		} else { and = true; }
    		
    		if(def.getIsAutoIncrement()) writeAutoIncrement(def, writer);
    	}
    	
    	return writer;
	}
    
    /**
     * Constructs a table alter statement to add AUTO_INCREMENT or
     * IDENTITY(1,1) or whatever the auto incrementing column type is for a
     * given database platform. In cases such as MySql/MariaDB - auto
     * increment must be the PK and there can only be one per table. It is
     * important to note that this function does not add the primary key
     * designation to a field and as such, all other alterations must occur
     * before this function is called.
     * @param array
     * @param writer
     * @throws IOException 
     * @throws DbTypeNotMappedException 
     */
    public void writeAutoIncrement(ColumnDefinition column, Writer writer) throws DbTypeNotMappedException, IOException {
    	// ALTER TABLE `document` MODIFY `document_id` INT AUTO_INCREMENT;
    	writer.write(String.format(
    			"ALTER TABLE %s MODIFY %s ", 
    			joinIdentifiers(column.getSchema(), column.getTable()),
    			escapeIdentifier(column.getName())));
    	writeDbType(column, writer);
    	writer.write(" AUTO_INCREMENT;");
	}

	private String joinAndEscape(String delim, String... idents) {
    	String[] results = new String[idents.length];
    	
    	for(int i = 0; i < idents.length; i++) {
    		results[i] = escapeIdentifier(idents[i]);
    	}
    	
    	return Helpers.join(delim, results);
    }
    
    public Writer writeConstraints(Writer writer, Constraint...constraints) throws IOException, HikariDbmException {
    	boolean and = false;
    	
    	for(Constraint c : constraints) {
    		if(and) {
    			writer.write(Helpers.EOL);
    		} else { and = true; }
    		
    		writeConstraint(c, writer);
    	}
    	
    	return writer;
    }
    
	public Writer writeConstraint(Constraint constraint, Writer writer) throws HikariDbmException, IOException {
    	String prefix = "";
    	String constraintType = "";
    	String genMods = "";

    	if(Helpers.isNullOrEmpty(constraint.getTable())) throw new MissingParameterException("Table Name");
    	if(Helpers.isNullOrEmpty(constraint.getName())) throw new MissingParameterException("Constraint Name");
    	
        switch(constraint.getConstraintType())
        {
        	/*
            case CheckConstraint.class.getName():
                prefix = "CK";
                constraintType = "CHECK";
                genMods = String.format("(%s)", Helpers.join(" ", mods).trim());
                break;
         	*/
            case DefaultConstraint.CONSTRAINT_TYPE:
            	DefaultConstraint df = (DefaultConstraint)constraint;
                prefix = "DF";
                constraintType = "DEFAULT";
                genMods = String.format("%s FOR %s", df.getDefaultValue(), escapeIdentifier(df.getField()));
                break;
            case ForeignKeyConstraint.CONSTRAINT_TYPE:
            	ForeignKeyConstraint fk = (ForeignKeyConstraint)constraint;
                prefix = "FK";
                constraintType = "FOREIGN KEY";
                // mods[0] = Current Table Key
                // mods[1] = Target Schema
                // mods[2] = Target Table
                // mods[3] = Target Column
                genMods = String.format("(%s) REFERENCES %s(%s)",
                		escapeIdentifier(fk.getField()), 
                		joinIdentifiers(fk.getTargetSchema(), fk.getTargetTable()), 
                		escapeIdentifier(fk.getTargetField()));
                break;
            case PrimaryKeyConstraint.CONSTRAINT_TYPE:
            	PrimaryKeyConstraint pk = (PrimaryKeyConstraint)constraint;
            	if(pk.getFields().length == 0) throw new MissingParameterException("Primary Key Constraint Fields Are Missing");
                prefix = "PK";
                constraintType = "PRIMARY KEY";
                genMods = String.format("(%s)", joinAndEscape(", ", pk.getFields()));
                break;
            case UniqueConstraint.CONSTRAINT_TYPE:
            	UniqueConstraint uq = (UniqueConstraint)constraint;
            	if(uq.getFields().length == 0) throw new MissingParameterException("Unique Constraint Fields Are Missing");
                prefix = "UQ";
                constraintType = "UNIQUE";
                genMods = String.format("(%s)", joinAndEscape(", ", uq.getFields()));
                break;
            default: throw new UnknownConstraintException(String.format("Unknown Constraint Type: %s", constraint.getClass().getName()));
        }
        
        String result = String.format("ALTER TABLE %s ADD CONSTRAINT %s_%s %s %s;", 
        		joinIdentifiers(constraint.getSchema(), constraint.getTable()), prefix, 
        		constraint.getName(), constraintType, genMods);
		writer.write(result);
        return writer;
	}
}
