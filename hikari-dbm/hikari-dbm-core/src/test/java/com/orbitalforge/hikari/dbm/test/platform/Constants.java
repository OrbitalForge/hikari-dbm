package com.orbitalforge.hikari.dbm.test.platform;

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

public class Constants {
	public static final String ALTER_TABLE_FK = "ALTER TABLE sSchema.sTable ADD CONSTRAINT FK_fkName FOREIGN KEY (sField) REFERENCES tSchema.tTable(tField);";
	public static final String ALTER_TABLE_UQ = "ALTER TABLE sSchema.sTable ADD CONSTRAINT UQ_uqName UNIQUE (sField);";
	public static final String ALTER_TABLE_PK = "ALTER TABLE sSchema.sTable ADD CONSTRAINT PK_pkName PRIMARY KEY (sField);";
	public static final String BASIC_COLUMN = "\"TEST\" nvarchar(MAX) NOT NULL";
	public static final String COLUMN_NULL_DEFAULT = "ALTER TABLE sSchema.sTable ALTER COLUMN TEST SET DEFAULT NULL;";
	public static final String COLUMN_BASIC_DEFAULT = "ALTER TABLE sSchema.sTable ALTER COLUMN TEST SET DEFAULT 'String Value';";
	public static final String COLUMN_NUMERIC_DEFAULT = "ALTER TABLE sSchema.sTable ALTER COLUMN TEST SET DEFAULT 15.0;";
	public static final String COLUMN_FUNC_DEFAULT = "ALTER TABLE sSchema.sTable ALTER COLUMN TEST SET DEFAULT TIMESTAMP;";
	public static final String[] BASIC_TABLE = new String[] { 
		"CREATE TABLE account ( ",
		"id bigint NOT NULL, ",
		"kubo varchar(767) NOT NULL, ",
		"monies decimal(18,4) NOT NULL );",
		"ALTER TABLE account ADD CONSTRAINT UQ_SAMPLE UNIQUE (kubo);",
		"ALTER TABLE account ADD CONSTRAINT PK_sample PRIMARY KEY (id);",
		"ALTER TABLE account MODIFY id bigint AUTO_INCREMENT;",
		"ALTER TABLE account ALTER COLUMN kubo SET DEFAULT 'Quality';"
	};
}
