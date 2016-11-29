package com.orbitalforge.hikari.dbm.db.query;

public class Criteria {
	private String identifier;
	private LogicalOperator operator;
	private Object value;
	
	public Criteria(String identifier, LogicalOperator operator, Object value) {
		this.identifier = identifier;
		this.operator = operator;
		this.value = value;
	}
	
	public Criteria(String identifier, LogicalOperator operator) {
		this.identifier = identifier;
		this.operator = operator;
	}
}
