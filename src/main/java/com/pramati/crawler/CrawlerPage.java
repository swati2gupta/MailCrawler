package com.pramati.crawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerPage {

	
	private Document htmlDocument;

	public List<String> crawl(String url) {
		List<String> links = new LinkedList<String>();
		try {
			Document document = Jsoup.connect(url).get();
			// String patternToMatch="ESMTP id [a-zA-Z0-9]+";
			this.htmlDocument = document;
			Elements linksOnPage = htmlDocument.select("a[href]");
			System.out.println("Found (" + linksOnPage.size() + ") links");
			for (Element link : linksOnPage) {
				links.add(link.absUrl("href"));
			}
		} catch (IOException ioe) {
			//return false;
		}
		return links;
	}

	public boolean searhForMail(String searchWord) {
		String patternToMatch = searchWord;
		String htmlString = htmlDocument.toString();
		// System.out.println("document" + htmlString);
		IMailFilter filter = new DateBasedFilter();
		return (filter.evaluate(patternToMatch, htmlString));
	}

}
