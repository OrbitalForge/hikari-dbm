package com.orbitalforge.hikari.dbm.test.platform;

public class Constants {
	public static final String ALTER_TABLE_FK = "ALTER TABLE sSchema.sTable ADD CONSTRAINT FK_fkName FOREIGN KEY (sField) REFERENCES tSchema.tTable(tField);";
	public static final String ALTER_TABLE_UQ = "ALTER TABLE sSchema.sTable ADD CONSTRAINT UQ_uqName UNIQUE (sField);";
}
