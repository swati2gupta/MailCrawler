package com.pramati.crawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Crawler {
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();

	public void search(String url, String keyword) {
		do {
			String currentUrl;
			CrawlerPage crawlPage = new CrawlerPage();
			if (this.pagesToVisit.isEmpty()) {
				currentUrl = url;
				this.pagesVisited.add(url);
			} else {
				currentUrl = this.nextUrl();
			}
			this.pagesToVisit.addAll(crawlPage.crawl(currentUrl));
			boolean success = crawlPage.searhForMail(keyword);
			if (success) {
				String libFile = "/home/swatig/Desktop/mail2014";
				Download mail = new DownloadFile();
				mail.downloadMail(currentUrl, libFile, ".txt");

			}
			

			System.out.println("\n**Done** Visited " + this.pagesVisited.size()
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