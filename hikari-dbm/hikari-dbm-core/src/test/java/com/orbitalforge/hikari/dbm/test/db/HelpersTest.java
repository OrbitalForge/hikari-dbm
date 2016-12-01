package com.orbitalforge.hikari.dbm.test.db;

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

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import com.orbitalforge.hikari.dbm.db.Helpers;

public class HelpersTest {
	
	@BeforeMethod
	protected void setUp() {
		
	}
	
	@Test
	public void test_join() {
		Assert.assertEquals(Helpers.join(", ", "SAMPLE", "SAMPLE2"), "SAMPLE, SAMPLE2");
	}
	
	@Test
	public void test_parseBoolean() {
		// (TRUE) Standard Range
		Assert.assertEquals(Helpers.parseBoolean("YES"), true);
		Assert.assertEquals(Helpers.parseBoolean("1"), true);
		Assert.assertEquals(Helpers.parseBoolean(1), true);
		Assert.assertEquals(Helpers.parseBoolean("TRUE"), true);
		
		// (TRUE) Mixed Case
		Assert.assertEquals(Helpers.parseBoolean("TrUe"), true);
		Assert.assertEquals(Helpers.parseBoolean("YeS"), true);
		
		// (FALSE) Standard Range
		Assert.assertEquals(Helpers.parseBoolean("FALSE"), false);
		Assert.assertEquals(Helpers.parseBoolean("0"), false);
		Assert.assertEquals(Helpers.parseBoolean(0), false);
		Assert.assertEquals(Helpers.parseBoolean("NO"), false);
		
		// (FALSE) Mixed Case
		Assert.assertEquals(Helpers.parseBoolean("No"), false);
		Assert.assertEquals(Helpers.parseBoolean("FaLse"), false);
		
		// (FALSE) Edge Cases
		Assert.assertEquals(Helpers.parseBoolean(null), false);
		Assert.assertEquals(Helpers.parseBoolean(8665), false);
		Assert.assertEquals(Helpers.parseBoolean(-65535), false);
		Assert.assertEquals(Helpers.parseBoolean("Jibberish"), false);
	}
	
	@Test
	public void test_readResults() {
		// throw new RuntimeException("Not Implemented");
	}
	
	@Test
	public void test_getColumns() {
		// throw new RuntimeException("Not Implemented");
	}
}
