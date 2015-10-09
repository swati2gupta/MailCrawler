package com.pramati.crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Properties;

public class Crawler {
	private Set<String> pagesVisited = new HashSet<String>();
	private Set<String> pagesToVisit = new HashSet<String>();
	final static Logger logger = Logger.getLogger(Crawler.class);
	Properties configFile = new Properties();

	public Document getHtmlDoc(String url) throws IOException {
		Document document = Jsoup.connect(url).get();
		return document;
	}

	public void search(String url, String keyword) {
		try
		{
		do {
			String currentUrl;
			PageParser crawlPage = new PageParser();
			// Document doc=getHtmldoc(currentUrl);
			if (this.pagesToVisit.isEmpty()) {
				currentUrl = url;
				this.pagesVisited.add(url);
			} else {
				currentUrl = this.nextUrl();
			}
			logger.debug("Current url:" + currentUrl);
			try {
				boolean success = crawlPage.searhForMail(
						getHtmlDoc(currentUrl), keyword);
				logger.debug("search for mail true or false" + success);
				if (success) {
					try {
						configFile.load(Crawler.class.getClassLoader()
								.getResourceAsStream("config.properties"));
						String dpath = configFile.getProperty("downloadPath");
						String libFile = dpath
								+ keyword
								+ currentUrl.substring(
										currentUrl.indexOf("@") - 15,
										currentUrl.indexOf("@") - 3);
						Download mail = new DownloadFile();
						mail.downloadMail(currentUrl, libFile, ".txt");
					} catch (Exception e) {
						logger.error("Exception in opening properties file", e);
					}

				} else {
					this.pagesToVisit.addAll(crawlPage.crawl(
							getHtmlDoc(currentUrl), keyword));
				}
			} catch (Exception e) {
				logger.error("Exception in getting HTML document from teh URL",
						e);
			}

			logger.debug("\n**Done** Visited " + this.pagesVisited.size()
					+ " web page(s)");
			logger.debug("\nTo Visit pages " + this.pagesToVisit.size()
					+ " web page(s)");
		} while (!this.pagesToVisit.isEmpty());
		}
		catch(Exception e)
		{
			logger.debug("Exception in getting teh next url",e);
		}

	}

	private String nextUrl()  throws Exception{
		String nextUrl;
		Iterator<String> it = this.pagesToVisit.iterator();
		//nextUrl=it.next().toString();
	    do 
	    {
	    	nextUrl=it.next().toString();
	    	it.remove();
	    }while(this.pagesVisited.contains(nextUrl) );
	    
	    this.pagesVisited.add(nextUrl);
		return nextUrl;
	}
}