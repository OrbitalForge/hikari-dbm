package com.orbitalforge.hikari.dbm.schemaframework;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.orbitalforge.hikari.dbm.db.AbstractDbQueries;
import com.orbitalforge.hikari.dbm.db.AbstractDbService;
import com.orbitalforge.hikari.dbm.db.Helpers;
import com.orbitalforge.hikari.dbm.db.NamedParameterStatement;
import com.orbitalforge.hikari.dbm.exception.HikariDbmException;

public class SchemaManager {
	
	private final AbstractDbService dbService;
	
	public SchemaManager(AbstractDbService dbService) {
		this.dbService = dbService;
	}
	
	public void createTable(TableDefinition table) throws HikariDbmException, IOException, SQLException {
		Writer writer = table.buildCreationWriter(dbService.getPlatform(), new StringWriter());
		Connection connection = dbService.getDataSource().getConnection();
		Statement statement = connection.createStatement();
		statement.execute(writer.toString());
		System.out.println(writer.toString());
	}

	public String[] getTableNames() throws SQLException {
		Connection connection = dbService.getDataSource().getConnection();
		
		NamedParameterStatement nps = new NamedParameterStatement(connection, AbstractDbQueries.GET_TABLES);
		addNamedParameters(dbService, connection, nps);
		
		ResultSet queryResults = nps.executeQuery();
		ArrayList<String> results = new ArrayList<String>();
		
		while(queryResults.next()) {
			results.add(dbService.getPlatform().joinIdentifiers(
					queryResults.getString(queryResults.findColumn("table_schema")),
					queryResults.getString(queryResults.findColumn("table_name"))));
		}
		
		nps.close();
		connection.close();
		
		String[] finalResults = new String[results.size()];
		results.toArray(finalResults);
		return finalResults;
	}
	
	private void addNamedParameters(AbstractDbService ds, Connection conn, NamedParameterStatement statement) throws SQLException {
		statement.setString("catalog", conn.getCatalog());
		statement.setString("schema", conn.getSchema());
	}

	/**
	 * Returns a table definition based on INFORMATION_SCHEMA definitions.
	 * @param schema - This is a user in Oracle or a Database in MySql/MariaDb
	 * @param table
	 * @return Returns a TableDefinition if successful or null if it's not found.
	 * @throws SQLException 
	 */
	public TableDefinition getTable(String schema, String table) throws SQLException {
		Connection connection = dbService.getDataSource().getConnection();
		NamedParameterStatement nps = new NamedParameterStatement(connection, AbstractDbQueries.GET_TABLE_SINGLE);
		addNamedParameters(dbService, connection, nps);
		
		// Set Query Params
		nps.setString("schema", schema);
		nps.setString("table", table);
		
		/* Read Columns */
		ResultSet queryResults = nps.executeQuery();
		List<Map<String, Object>> results = Helpers.readResults(queryResults);	
		if(results.size() != 1) return null;
		
		TableDefinition definition = new TableDefinition(results.get(0));
		definition.addColumns(getTableColumns(schema, table));
		
		nps.close(); // Auto-Closes Query Results
		connection.close();
		
		return definition;
	}
	
	public ColumnDefinition[] getTableColumns(String schema, String table) throws SQLException {
		Connection connection = dbService.getDataSource().getConnection();
		NamedParameterStatement nps = new NamedParameterStatement(connection, AbstractDbQueries.GET_TABLE_COLUMNS);
		addNamedParameters(dbService, connection, nps);
		
		// Set Query Params
		nps.setString("schema", schema);
		nps.setString("table", table);
		
		/* Read Columns */
		ResultSet queryResults = nps.executeQuery();
		List<Map<String, Object>> results = Helpers.readResults(queryResults);	
		ColumnDefinition[] columns = new ColumnDefinition[results.size()];
		
		for(int i = 0; i < results.size(); i++) {
			columns[i] = new ColumnDefinition(results.get(i));
			// Parse Data Type
			String dataType = results.get(i).get("data_type").toString().toLowerCase();
			columns[i].setDbType(dbService.getPlatform().getColumnType(dataType));
		}
		
		nps.close(); // Auto-Closes Query Results
		connection.close();
		
		return columns;
	}

	public void updateTable(TableDefinition createSampleTable) {
	}
}
