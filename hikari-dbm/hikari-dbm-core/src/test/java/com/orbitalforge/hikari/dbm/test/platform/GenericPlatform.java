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

import java.io.IOException;
import java.io.Writer;

import com.orbitalforge.hikari.dbm.platform.AbstractDbPlatform;
import com.orbitalforge.hikari.dbm.schemaframework.ColumnDefinition;

public class GenericPlatform extends AbstractDbPlatform {
	@Override
	public void setup() {
	}
	
	public void setIdentifierFormat(String format) {
		this.identifierFormat = format;
	}

	@Override
	public void buildAutoIncrement(ColumnDefinition columnDefinition, Writer writer) throws IOException {
		writer.write(" AUTO_INCREMENT");
	}
}