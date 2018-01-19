package com.epicxrm.hikari.dbm.db;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Converts database types to Java class types.
 * https://stackoverflow.com/questions/5251140/map-database-type-to-concrete-java-class
 */
public class SqlTypeMap {
	private static Map<Class<?>, Integer> classTypeMap = new HashMap<Class<?>, Integer>();
	
	static {
		classTypeMap.put(String.class, Types.LONGVARCHAR);
		classTypeMap.put(BigDecimal.class, Types.DECIMAL);
		classTypeMap.put(Boolean.class, Types.BIT);
		classTypeMap.put(byte.class,  Types.TINYINT);
		classTypeMap.put(Byte.class, Types.TINYINT);
		classTypeMap.put(short.class, Types.SMALLINT);
		classTypeMap.put(Short.class, Types.SMALLINT);
		classTypeMap.put(int.class, Types.INTEGER);
		classTypeMap.put(Integer.class, Types.INTEGER);
		classTypeMap.put(long.class, Types.BIGINT);
		classTypeMap.put(Long.class, Types.BIGINT);
		classTypeMap.put(float.class, Types.FLOAT);
		classTypeMap.put(Float.class, Types.FLOAT);
		classTypeMap.put(double.class, Types.DOUBLE);
		classTypeMap.put(Double.class, Types.DOUBLE);
		classTypeMap.put(byte[].class, Types.VARBINARY);
		classTypeMap.put(Byte[].class, Types.VARBINARY);
		classTypeMap.put(Date.class, Types.DATE); // NOTE: This could need some additional research regarding SQL Date vs Date
	}
	
	public static int fromClass(Class<?> clazz) {
		if(classTypeMap.containsKey(clazz)) {
			return classTypeMap.get(clazz);
		}
		
		throw new RuntimeException(String.format("Unable to mapp Sql Type to %s", clazz.getCanonicalName()));
	}
	
    /**
     * Translates a data type from an integer (java.sql.Types value) to a string
     * that represents the corresponding class.
     * 
     * @param type
     *            The java.sql.Types value to convert to its corresponding class.
     * @return The class that corresponds to the given java.sql.Types
     *         value, or Object.class if the type has no known mapping.
     */
    public static Class<?> toClass(int type) {
        Class<?> result = Object.class;

        switch (type) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                result = String.class;
                break;

            case Types.NUMERIC:
            case Types.DECIMAL:
                result = java.math.BigDecimal.class;
                break;

            case Types.BIT:
                result = Boolean.class;
                break;

            case Types.TINYINT:
                result = Byte.class;
                break;

            case Types.SMALLINT:
                result = Short.class;
                break;

            case Types.INTEGER:
                result = Integer.class;
                break;

            case Types.BIGINT:
                result = Long.class;
                break;

            case Types.REAL:
            case Types.FLOAT:
                result = Float.class;
                break;

            case Types.DOUBLE:
                result = Double.class;
                break;

            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
                result = Byte[].class;
                break;

            case Types.DATE:
                result = java.sql.Date.class;
                break;

            case Types.TIME:
                result = java.sql.Time.class;
                break;

            case Types.TIMESTAMP:
                result = java.sql.Timestamp.class;
                break;
        }

        return result;
    }
}