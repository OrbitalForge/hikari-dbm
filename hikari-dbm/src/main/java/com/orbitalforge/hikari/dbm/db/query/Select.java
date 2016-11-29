package com.orbitalforge.hikari.dbm.db.query;

public class Select extends Query {
	private String from;
	private WhereClause where = new WhereClause(LogicalGroup.And);
	private String[] columns;
	
	public Select columns(String... columns) {
		this.columns = columns;
		return this;
	}
	
	public Select from(String qualifier) {
		this.from = qualifier;
		return this;
	}
	
	public WhereClause where(LogicalGroup group) {
		return where.addClause(new WhereClause(group));
	}	
}
