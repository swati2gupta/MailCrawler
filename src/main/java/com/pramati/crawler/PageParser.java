package com.pramati.crawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.log4j.Logger;

public class PageParser {
	Properties configFile = new Properties();
	final static Logger logger = Logger.getLogger(PageParser.class);

	public Document getHtmlDoc(String url) throws IOException {
		Document document = Jsoup.connect(url).get();
		return document;
	}

	public List<String> crawl(String url) throws IOException {
		List<String> links = new LinkedList<String>();
			try
			{
			configFile.load(Crawler.class.getClassLoader().getResourceAsStream(
					"config.properties"));
			configFile.getProperty("domain");
			}
			catch(IOException e)
			{
				logger.error("Exception in opening properties file",e);
			}
			
			Elements linksOnPage = getHtmlDoc(url).select("a[href]");
			logger.debug("Links found on current page: " + linksOnPage.size()
					+ ") links");
			for (Element link : linksOnPage) {
				if (link.absUrl("href").toLowerCase()
						.contains(configFile.getProperty("domain").toLowerCase())

				) {
					links.add(link.absUrl("href"));
				}
			}
		
		return links;
	}

	public boolean searhForMail(String url, String searchWord) throws IOException{
		boolean result = false;
		
			String patternToMatch = searchWord;
			String htmlString = getHtmlDoc(url).toString();
			// System.out.println("document" + htmlString);
			IMailFilter filter = new DateBasedFilter();
			if (filter.evaluate(patternToMatch, htmlString)) {
				logger.debug("keyword found in the page");
				result = true;
			}
		
		return result;
	}
}
