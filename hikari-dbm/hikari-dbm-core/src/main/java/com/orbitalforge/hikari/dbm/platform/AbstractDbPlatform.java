package com.orbitalforge.hikari.dbm.platform;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

import com.orbitalforge.hikari.dbm.db.Helpers;
import com.orbitalforge.hikari.dbm.exception.DbTypeNotMappedException;
import com.orbitalforge.hikari.dbm.exception.HikariDbmException;
import com.orbitalforge.hikari.dbm.schemaframework.CheckConstraint;
import com.orbitalforge.hikari.dbm.schemaframework.ColumnDefinition;
import com.orbitalforge.hikari.dbm.schemaframework.Constraint;
import com.orbitalforge.hikari.dbm.schemaframework.DefaultConstraint;
import com.orbitalforge.hikari.dbm.schemaframework.ForeignKeyConstraint;
import com.orbitalforge.hikari.dbm.schemaframework.PrimaryKeyConstraint;
import com.orbitalforge.hikari.dbm.schemaframework.TableDefinition;
import com.orbitalforge.hikari.dbm.schemaframework.UniqueConstraint;

public abstract class AbstractDbPlatform {
	private Map<Integer, String> columnTypeMap = new HashMap<Integer, String>();
	private Map<String, Integer> reverseColumnTypeMap = new HashMap<String, Integer>();
	protected String identifierFormat = "\"%s\"";
	
	public String escapeIdentifier(String identifier) {
		String cleaned = identifier.replaceAll("\"", "").replaceAll("\'", "");
		if(cleaned == "") return cleaned;
		return String.format(identifierFormat, cleaned);
	}

	public String joinIdentifiers(String... identifiers) {
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
		// this.reverseColumnTypeMap.put(type, dbType);
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

	public int getColumnType(String dataType) {
		// TODO Auto-generated method stub
		return 0;
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

		writer.write(" ");
		writer.write(typeTemplate);
	}

	protected abstract void buildAutoIncrement(ColumnDefinition columnDefinition, Writer writer) throws IOException;

	private Writer writeColumn(ColumnDefinition column, Writer writer) throws HikariDbmException, IOException {
		writer.write(this.escapeIdentifier(column.getName()));
		writeDbType(column, writer);
		if (column.getIsNullable())
			writer.write(" NOT NULL");
		if (column.getIsAutoIncrement())
			this.buildAutoIncrement(column, writer);
		if (column.getIsPrimaryKey())
			writer.write(" PRIMARY KEY");

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

		for (int i = 0; i < columnDefs.length; i++) {
			columns[i] = writeColumn(columnDefs[i], new StringWriter()).toString();
		}

		writer.write(Helpers.join(", " + newLine, columns));

		writer.write(" );");
		writer.write(newLine);
		writeConstraints(writer, table.getConstraints());
		
		return writer;
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
    	String newLine = System.getProperty("line.separator");
    	
    	for(Constraint c : constraints) {
    		if(and) {
    			writer.write(newLine);
    		} else { and = true; }
    		
    		writeConstraint(c, writer);
    	}
    	
    	return writer;
    }
    
	public Writer writeConstraint(Constraint constraint, Writer writer) throws HikariDbmException, IOException {
    	String prefix = "";
    	String constraintType = "";
    	String genMods = "";

        switch(constraint.getConstraintType())
        {
        /*
            case CheckConstraint.class.getName():
                prefix = "CK";
                constraintType = "CHECK";
                genMods = String.format("(%s)", Helpers.join(" ", mods).trim());
                break;
            case DefaultConstraint.class.getName():
                prefix = "DF";
                constraintType = "DEFAULT";
                genMods = String.format("{0} FOR {1}", mods[1], escapeIdentifier(mods[0]));
                break;
                */
            case "FK":
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
            case "PK":
            	PrimaryKeyConstraint pk = (PrimaryKeyConstraint)constraint;
                prefix = "PK";
                constraintType = "PRIMARY KEY";
                genMods = String.format("(%s)", joinAndEscape(", ", pk.getFields()));
                break;
            case "UQ":
            	UniqueConstraint uq = (UniqueConstraint)constraint;
                prefix = "UQ";
                constraintType = "UNIQUE";
                genMods = String.format("(%s)", joinAndEscape(", ", uq.getFields()));
                break;
            default: throw new HikariDbmException(String.format("Unknown Constraint Type: %s", constraint.getClass().getName()));
        }
        
        String result = String.format("ALTER TABLE %s ADD CONSTRAINT %s_%s %s %s;", 
        		joinIdentifiers(constraint.getSchema(), constraint.getTable()), prefix, 
        		constraint.getName().toUpperCase(), constraintType, genMods);
		writer.write(result);
        return writer;
	}
}
