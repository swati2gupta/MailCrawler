package com.pramati.crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CrawlerThread implements Runnable {
	protected static BlockingQueue<String> pagesToVisit = null;
	protected static BlockingQueue<String> downloadQueue = null;
	protected static Set<String> pagesVisited = null;
	Set<String> link = new HashSet<String>();
	final static Logger logger = Logger.getLogger(CrawlerThread.class);

	public CrawlerThread(BlockingQueue<String> urlQueue,
			Set<String> visitedSet, BlockingQueue<String> downlQueue) {
		pagesToVisit = urlQueue;
		pagesVisited = visitedSet;
		downloadQueue = downlQueue;
	}

	public void run() {
		try {
			while (pagesToVisit.isEmpty()) {
				Thread.sleep(10);
			}
			while (!pagesToVisit.isEmpty()) {
				logger.debug("Thread" + Thread.currentThread().getName());
				String currentUrl = nextUrl();
				search(currentUrl, "2014");
			}
		} catch (Exception e) {
			logger.error("Exception in getting the next URl", e);
		}
		logger.debug(" i am out of while");
	}

	public void search(String url, String keyword) {
		try {
			String currentUrl = url;
			PageParser crawlPage = new PageParser();
			logger.debug("Current url:" + currentUrl);
			try {
				boolean success = crawlPage.searhForMail(
						getHtmlDoc(currentUrl), keyword);
				logger.debug("search for mail true or false" + success);
				if (success) {
					downloadQueue.put(currentUrl);
				} else {
					logger.debug("in else");
					link = crawlPage.crawl(getHtmlDoc(currentUrl), keyword);
					for (String s : link)
						pagesToVisit.put(s);
				}
			} catch (Exception e) {
				logger.error("Exception in getting HTML document from teh URL",
						e);
			}

			logger.debug("\n**Done** Visited " + pagesVisited.size()
					+ " web page(s)");
			logger.debug("\nTo Visit pages " + pagesToVisit.size()
					+ " web page(s)");
			logger.debug("\ndownload Queue" + downloadQueue.size()
					+ " web page(s)");
		} catch (Exception e) {
			logger.debug("Exception in getting the next url", e);
		}

	}

	public Document getHtmlDoc(String url) throws IOException {
		Document document = Jsoup.connect(url).get();
		return document;
	}

	public String nextUrl() throws Exception {
		String nextUrl;
		do {
			nextUrl = pagesToVisit.take();
		} while (pagesVisited.contains(nextUrl) && !pagesToVisit.isEmpty());
		pagesVisited.add(nextUrl);
		return nextUrl;
	}
}
