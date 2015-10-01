package com.pramati.crawler;

import java.util.Scanner;

import org.apache.log4j.Logger;

public class StartCrawler {
	final static Logger logger = Logger.getLogger(StartCrawler.class);
	public static void main(String args[]) {
		logger.debug("Enter the URl to Parse");
		Scanner scanner = new Scanner(System.in);
		String URL = scanner.next();
		Crawler mailcrawl = new Crawler();
		mailcrawl.search(URL, "2014");
		scanner.close();
	}

}
