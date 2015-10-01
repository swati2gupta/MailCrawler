package com.pramati.crawler;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CrawlerThread implements Runnable {
	protected BlockingQueue<String> pagesToVisit = null;
	protected BlockingQueue<String> pagesVisited = null;
	final static Logger logger = Logger.getLogger(Crawler.class);
	Properties configFile = new Properties();

	public CrawlerThread(BlockingQueue<String> urlQueue,
			BlockingQueue<String> visitedQueue) {
		this.pagesToVisit = urlQueue;
		this.pagesVisited = visitedQueue;

	}

	public void run() {
		try {
			String currentUrl = pagesToVisit.take();
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
							configFile.load(Crawler.class.getClassLoader()
									.getResourceAsStream("config.properties"));
							String dpath = configFile
									.getProperty("downloadPath");
							String libFile = dpath
									+ keyword
									+ currentUrl.substring(
											currentUrl.indexOf("@") - 15,
											currentUrl.indexOf("@") - 3);
							Download mail = new DownloadFile();
							mail.downloadMail(currentUrl, libFile, ".txt");
						} catch (Exception e) {
							logger.error(
									"Exception in opening properties file", e);
						}

					} else {
						this.pagesToVisit.addAll(crawlPage.crawl(
								getHtmlDoc(currentUrl), keyword));
					}
				} catch (Exception e) {
					logger.error(
							"Exception in getting HTML document from teh URL",
							e);
				}

				logger.debug("\n**Done** Visited " + this.pagesVisited.size()
						+ " web page(s)");
				logger.debug("\nTo Visit pages " + this.pagesToVisit.size()
						+ " web page(s)");
			} while (!this.pagesToVisit.isEmpty());
		} catch (Exception e) {
			logger.debug("Exception in getting teh next url", e);
		}

	}

	public Document getHtmlDoc(String url) throws IOException {
		Document document = Jsoup.connect(url).get();
		return document;
	}
}
