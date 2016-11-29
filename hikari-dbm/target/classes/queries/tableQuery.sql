SELECT 
	TABLE_CATALOG, 
	TABLE_SCHEMA,
	TABLE_NAME,
	TABLE_TYPE
FROM INFORMATION_SCHEMA.TABLES
WHERE
	TABLE_CATALOG = :catalog AND 
	TABLE_SCHEMA NOT IN (
		'INFORMATION_SCHEMA',
		'performance_schema',	-- Maria / MySql
		'mysql',				-- Maria / MySql
		'sys'					-- Microsoft SQL
	)