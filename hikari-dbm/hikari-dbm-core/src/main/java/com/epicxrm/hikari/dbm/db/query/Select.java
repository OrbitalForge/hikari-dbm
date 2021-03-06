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
