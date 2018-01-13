package com.epicxrm.hikari.dbm.db.query;

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
