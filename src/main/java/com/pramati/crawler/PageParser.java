package com.pramati.crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageParser {
	Properties configFile = new Properties();
	final static Logger logger = Logger.getLogger(PageParser.class);

	public Set<String> crawl(Document url, String searchWord)
			throws IOException {
		Set<String> links = new HashSet<String>();
		try {
			configFile.load(Crawler.class.getClassLoader().getResourceAsStream(
					"config.properties"));
			configFile.getProperty("domain");
		} catch (IOException e) {
			logger.error("Exception in opening properties file", e);
		}

		Elements linksOnPage = url.select("a[href]");
		String domainchk = configFile.getProperty("domain") + searchWord;
		logger.debug("Links found on current page: " + linksOnPage.size()
				+ ") links");
		for (Element link : linksOnPage) {
			if (link.absUrl("href").toLowerCase()
					.contains(domainchk.toLowerCase())

			) {
				links.add(link.absUrl("href"));
			}
		}

		return links;
	}

	public boolean searhForMail(Document doc, String searchWord)
			throws IOException {
		boolean result = false;

		String patternToMatch = searchWord;
		String htmlString = doc.toString();
		// System.out.println("document" + htmlString);
		IMailFilter filter = new DateBasedFilter();
		if (filter.evaluate(patternToMatch, htmlString)) {
			logger.debug("keyword found in the page");
			result = true;
		}

		return result;
	}

	/*
	 * public boolean parsePage(String url, String searchWord) throws
	 * IOException { Document doc=getHtmlDoc(url); Boolean result=false;
	 * Elements linksOnPage = doc.select("a[href]"); if(linksOnPage.size()==0) {
	 * String patternToMatch = searchWord; String htmlString = doc.toString();
	 * IMailFilter filter = new DateBasedFilter(); if
	 * (filter.evaluate(patternToMatch, htmlString)) {
	 * logger.debug("keyword found in the page"); result=true; } } return
	 * result; }
	 */
}
