package com.epicxrm.hikari.dbm.schemaframework;

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
}
