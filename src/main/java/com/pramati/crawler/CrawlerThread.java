package com.pramati.crawler;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CrawlerThread implements Runnable {
	protected static BlockingQueue<String> pagesToVisit = null;
	protected static BlockingQueue<String> downloadQueue = null;
	protected static Set<String> pagesVisited = null;
	final static Logger logger = Logger.getLogger(CrawlerThread.class);

	public CrawlerThread(BlockingQueue<String> urlQueue,
			Set<String> visitedQueue, BlockingQueue<String> downlQueue) {
		pagesToVisit = urlQueue;
		pagesVisited = visitedQueue;
		downloadQueue = downlQueue;
	}

	public void run() {
		try {
			logger.debug("Thread" + Thread.currentThread().getName());
			String currentUrl = nextUrl();
			pagesVisited.add(currentUrl);
			search(currentUrl, "2014");
		} catch (Exception e) {
			logger.error("Exception in getting the next URl", e);
		}
	}

	public void search(String url, String keyword) {
		try {
			do {
				String currentUrl = url;
				PageParser crawlPage = new PageParser();
				logger.debug("Current url:" + currentUrl);
				try {
					boolean success = crawlPage.searhForMail(
							getHtmlDoc(currentUrl), keyword);
					logger.debug("search for mail true or false" + success);
					if (success) {
						try {
							downloadQueue.add(currentUrl);
						} catch (Exception e) {
							logger.error(
									"Exception in opening properties file", e);
						}

					} else { logger.debug("in else");
						pagesToVisit.addAll(crawlPage.crawl(
								getHtmlDoc(currentUrl), keyword));
						logger.debug(pagesToVisit.size());
					}
				} catch (Exception e) {
					logger.error(
							"Exception in getting HTML document from teh URL",
							e);
				}

				logger.debug("\n**Done** Visited " + pagesVisited.size()
						+ " web page(s)");
				logger.debug("\nTo Visit pages " + pagesToVisit.size()
						+ " web page(s)");
			} while (!pagesToVisit.isEmpty());
		} catch (Exception e) {
			logger.debug("Exception in getting the next url", e);
		}

	}

	public Document getHtmlDoc(String url) throws IOException {
		Document document = Jsoup.connect(url).get();
		return document;
	}
	
	public String nextUrl() throws Exception{
		String nextUrl;
		//Iterator<String> it = pagesToVisit.iterator();
		//nextUrl=it.next().toString();
	    do 
	    {
	    	nextUrl=pagesToVisit.take();
	    }while(pagesVisited.contains(nextUrl));
	    
	    pagesVisited.add(nextUrl);
		return nextUrl;
	}
}
