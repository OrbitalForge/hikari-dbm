package com.orbitalforge.hikari.dbm.db.query;

import java.util.ArrayList;
import java.util.List;

public class WhereClause {
	private LogicalGroup group;
	private List<WhereClause> clauses = new ArrayList<WhereClause>();
	private List<Criteria> criteria = new ArrayList<Criteria>();
	private WhereClause parent;
	
	public WhereClause(LogicalGroup group) {
		this.group = group;
	}
	
	public WhereClause(WhereClause parent, LogicalGroup group) {
		this.group = group;
		this.parent = parent;
	}
	
	public WhereClause addClause(LogicalGroup group) {
		WhereClause clause = new WhereClause(this, group);
		this.clauses.add(clause);
		return clause;
	}
	
	public WhereClause addClause(WhereClause clause) {
		clause.setParent(this);
		this.clauses.add(clause);
		return clause;
	}
	
	public WhereClause criteria(String identifier, LogicalOperator operator, Object value) {
		criteria.add(new Criteria(identifier, operator, value));
		return this;
	}
	
	public WhereClause criteria(String identifier, LogicalOperator operator) {
		criteria.add(new Criteria(identifier, operator));
		return this;
	}
	
	public WhereClause getParent() {
		return parent;
	}
	
	public void setParent(WhereClause clause) {
		this.parent = parent;
	}
}
