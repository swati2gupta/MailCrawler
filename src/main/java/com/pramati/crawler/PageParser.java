package com.pramati.crawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.log4j.Logger;

public class PageParser {
	
	final static Logger logger = Logger.getLogger(PageParser.class.getName());
	
	public Document getHtmlDoc(String url) throws IOException {
		Document document = Jsoup.connect(url).get();
		return document;
	}

	public List<String> crawl(String url) {
		List<String> links = new LinkedList<String>();
		try {
			Elements linksOnPage = getHtmlDoc(url).select("a[href]");
			logger.debug("This is debug : " + linksOnPage.size() + ") links");
			System.out.println("Found (" + linksOnPage.size() + ") links");
			for (Element link : linksOnPage) {
				links.add(link.absUrl("href"));
			}
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
		return links;
	}

	public boolean searhForMail(String url, String searchWord) {
		boolean result = false;
		try {
			String patternToMatch = searchWord;
			String htmlString = getHtmlDoc(url).toString();
			// System.out.println("document" + htmlString);
			IMailFilter filter = new DateBasedFilter();
			if (filter.evaluate(patternToMatch, htmlString)) {
				System.out.println("keyword found");
				result = true;
			}
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
		return result;
	}
}
