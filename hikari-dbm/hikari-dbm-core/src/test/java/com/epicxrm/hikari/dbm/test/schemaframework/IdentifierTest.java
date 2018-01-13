package com.epicxrm.hikari.dbm.test.schemaframework;

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


import org.testng.AssertJUnit;
import org.testng.annotations.Test;

public class IdentifierTest extends GeneratorTest {

	@Test
	public void test_identifierEscape() {
		platform.setIdentifierFormat("\"%s\"");
		
		AssertJUnit.assertEquals("", platform.escapeIdentifier(""));
		AssertJUnit.assertEquals("", platform.escapeIdentifier(null));
		AssertJUnit.assertEquals("\"sample\"", platform.escapeIdentifier("sample"));
		AssertJUnit.assertEquals("\"sample.sample\"", platform.escapeIdentifier("sample.sample"));
		AssertJUnit.assertEquals("", platform.joinIdentifiers("", "", ""));
		AssertJUnit.assertEquals("\"sample\"", platform.joinIdentifiers("sample"));
		AssertJUnit.assertEquals("\"sample\".\"sample\"", platform.joinIdentifiers("sample", "sample"));
		AssertJUnit.assertEquals("", platform.joinIdentifiers((String)null));
		AssertJUnit.assertEquals("\"sample\"", platform.joinIdentifiers("sample", null));
		AssertJUnit.assertEquals("", platform.joinIdentifiers(null, null));
		AssertJUnit.assertEquals("\"sample\".\"sample\"", platform.joinIdentifiers(null, "sample", "sample", null));
	}
	
}
