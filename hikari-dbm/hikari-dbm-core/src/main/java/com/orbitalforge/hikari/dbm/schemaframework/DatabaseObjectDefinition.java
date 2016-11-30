package com.orbitalforge.hikari.dbm.schemaframework;

import java.util.HashMap;
import java.util.Map;

public abstract class DatabaseObjectDefinition {
	protected Map<String, Object> properties = new HashMap<String, Object>();

	// TODO: Allow null default values.
	@SuppressWarnings("unchecked")
	protected <T> T getProperty(String key, T defaultValue) {
		Object result = properties.get(key);
		if(result != null) {
			if(result.getClass() != defaultValue.getClass()) throw new RuntimeException("The returned value does not match the expected class!");
			return (T)result;
		}
		return defaultValue;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T getProperty(String key) {
		return (T) properties.get(key);
	}
	
	protected void setProperty(String key, Object value) {
		properties.put(key, value);
	}
	
	public String getName() { return getProperty("name"); }
	public void setName(String value) { setProperty("name", value); }
}
