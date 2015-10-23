package com.pramati.crawler;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class CrawlerThreadTest {
	protected static BlockingQueue<String> urlQueue = null;
	protected static BlockingQueue<String> downlQueue = null;
	protected static Set<String> visitedSet = null;
	CrawlerThread crawler=new CrawlerThread(urlQueue,visitedSet,downlQueue);
	@Test
	
	
	public void testCrawlerThread() {
		assertEquals(urlQueue,crawler.pagesToVisit);
		assertEquals(downlQueue,crawler.downloadQueue);
		assertEquals(visitedSet,crawler.pagesVisited);
	}

	@Test
	public void testRun() {
		fail("Not yet implemented");
	}

	@Test
	public void testSearch() {
		String url="http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/raw/%3C547C1A5F.7070709%40uni-jena.de%3E";
		crawler.search(url, "2014");
		assertEquals(true,downlQueue.contains(url));
		String url1="http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/browser";
		crawler.search(url1, "2014");
		assertEquals(true,urlQueue.contains(url1));
	
	}

	@Test
	public void testGetHtmlDoc() {
		String url="http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/raw/%3C547C1A5F.7070709%40uni-jena.de%3E";
		try
		{
		Document document = Jsoup.connect(url).get();
		assertEquals(document,crawler.getHtmlDoc(url));
		}
		catch(Exception e)
		{
			System.out.println("exception" + e);
		}
	}

	@Test
	public void testNextUrl() {
		String url="http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/browser";
		crawler.pagesToVisit.add(url);
		try
		{
		assertEquals(url,crawler.nextUrl());
		}
		catch(Exception e)
		{
			System.out.println("exception" + e);
		}
	
	}

}
