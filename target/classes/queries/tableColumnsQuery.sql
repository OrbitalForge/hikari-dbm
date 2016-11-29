SELECT 
	TABLE_CATALOG,
	TABLE_SCHEMA,
	TABLE_NAME,
	COLUMN_NAME,
	ORDINAL_POSITION,
	IS_NULLABLE,
	DATA_TYPE,
	CHARACTER_MAXIMUM_LENGTH,
	NUMERIC_PRECISION,
	NUMERIC_SCALE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE
	TABLE_CATALOG = :catalog AND
	TABLE_SCHEMA = :schema AND
	TABLE_NAME = :table