package com.orbitalforge.hikari.dbm.test.db;

import com.orbitalforge.hikari.dbm.db.Helpers;

import junit.framework.Assert;
import junit.framework.TestCase;

public class HelpersTest extends TestCase {
	
	protected void setUp() {
		
	}
	
	public void test_join() {
		Assert.assertEquals("SAMPLE, SAMPLE2", Helpers.join(", ", "SAMPLE", "SAMPLE2"));
	}
	
	public void test_parseBoolean() {
		// (TRUE) Standard Range
		Assert.assertEquals(true, Helpers.parseBoolean("YES"));
		Assert.assertEquals(true, Helpers.parseBoolean("1"));
		Assert.assertEquals(true, Helpers.parseBoolean(1));
		Assert.assertEquals(true, Helpers.parseBoolean("TRUE"));
		
		// (TRUE) Mixed Case
		Assert.assertEquals(true, Helpers.parseBoolean("TrUe"));
		Assert.assertEquals(true, Helpers.parseBoolean("YeS"));
		
		// (FALSE) Standard Range
		Assert.assertEquals(false, Helpers.parseBoolean("FALSE"));
		Assert.assertEquals(false, Helpers.parseBoolean("0"));
		Assert.assertEquals(false, Helpers.parseBoolean(0));
		Assert.assertEquals(false, Helpers.parseBoolean("NO"));
		
		// (FALSE) Mixed Case
		Assert.assertEquals(false, Helpers.parseBoolean("No"));
		Assert.assertEquals(false, Helpers.parseBoolean("FaLse"));
		
		// (FALSE) Edge Cases
		Assert.assertEquals(false, Helpers.parseBoolean(null));
		Assert.assertEquals(false, Helpers.parseBoolean(8665));
		Assert.assertEquals(false, Helpers.parseBoolean(-65535));
		Assert.assertEquals(false, Helpers.parseBoolean("Jibberish"));
	}
	
	public void test_readResults() {
		// throw new RuntimeException("Not Implemented");
	}
	
	public void test_getColumns() {
		// throw new RuntimeException("Not Implemented");
	}
}
