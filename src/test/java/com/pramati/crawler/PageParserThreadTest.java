package com.pramati.crawler;

import static org.junit.Assert.*;

import java.util.concurrent.BlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class PageParserThreadTest {
	protected static BlockingQueue<String> pagesToVisit = null;
	PageParserThread pageParser = new PageParserThread(pagesToVisit);

	@Test
	public void testPageParserThread() {
		assertEquals(pagesToVisit, pageParser.pagesToVisit);
	}

	@Test
	public void testCrawl() {
		String url = "http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/browser";
		try {
			Document document = Jsoup.connect(url).get();
			pageParser.crawl(document, "2014");
		} catch (Exception e) {
			System.out.println("exception" + e);
		}

		assertEquals(
				true,
				pagesToVisit
						.contains("http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/raw/%3C20141201165933.302fe3b8%40linux-du93.site%3E"));
	}

	@Test
	public void testSearhForMail() {
		String url = "http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/raw/%3C547C1A5F.7070709%40uni-jena.de%3E";
		try {
			Document document = Jsoup.connect(url).get();
			assertEquals(true, pageParser.searhForMail(document, "2014"));
		} catch (Exception e) {
			System.out.println("exception" + e);
		}
	}

}
