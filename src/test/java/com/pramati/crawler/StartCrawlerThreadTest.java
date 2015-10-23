package com.pramati.crawler;

import static org.junit.Assert.*;

import org.junit.Test;

public class StartCrawlerThreadTest {
	StartCrawlerThread startCrawler=new StartCrawlerThread();
	@Test
	public void testMain() {
		startCrawler.main();
	}

}
