package com.pramati.crawler;

import junit.framework.Assert;

import org.junit.Test;

public class DateBasedFilterTest {
	DateBasedFilter d = new DateBasedFilter();

	@Test
	public void testEvaluate() {
		Assert.assertTrue(d.evaluate("2014", "Date: May,04 2014"));

	}

}
