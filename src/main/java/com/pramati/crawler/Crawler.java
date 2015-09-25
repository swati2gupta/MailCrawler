package com.pramati.crawler;

import java.util.HashSet;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import java.util.Properties;

public class Crawler {
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();
	final static Logger logger = Logger.getLogger(Crawler.class);
	Properties configFile = new Properties();

	public void search(String url, String keyword) {
		do {
			String currentUrl;
			PageParser crawlPage = new PageParser();
			if (this.pagesToVisit.isEmpty()) {
				currentUrl = url;
				this.pagesVisited.add(url);
			} else {
				currentUrl = this.nextUrl();
			}
			logger.debug("Current url:" + currentUrl);
			try {
				boolean success = crawlPage.searhForMail(currentUrl, keyword);
				logger.debug("search for mail true or false" + success);
				if (success) {
					try {
						configFile.load(Crawler.class.getClassLoader()
								.getResourceAsStream("config.properties"));
						String dpath = configFile.getProperty("downloadPath");
						String libFile = dpath
								+ keyword
								+ currentUrl.substring(
										currentUrl.indexOf("%3C") + 1,
										currentUrl.indexOf("%3E") - 10);
						Download mail = new DownloadFile();
						mail.downloadMail(currentUrl, libFile, ".txt");
					} catch (Exception e) {
						logger.error("Exception in opening properties file", e);
					}

				}
				else
				{
					this.pagesToVisit.addAll(crawlPage.crawl(currentUrl));	
				}
			} catch (Exception e) {
				logger.error("Exception in getting HTML document from teh URL",
						e);
			}

			logger.debug("\n**Done** Visited " + this.pagesVisited.size()
					+ " web page(s)");
		} while (!this.pagesToVisit.isEmpty());

	}

	private String nextUrl() {
		String nextUrl;
		do {
			nextUrl = this.pagesToVisit.remove(0);
		} while (this.pagesVisited.contains(nextUrl));
		this.pagesVisited.add(nextUrl);
		return nextUrl;
	}

}