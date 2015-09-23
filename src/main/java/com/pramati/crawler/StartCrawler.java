package com.pramati.crawler;

import java.util.Scanner;

public class StartCrawler {

	public static void main(String args[]) {
		System.out.println("Enter the URl to Parse");
		Scanner scanner = new Scanner(System.in);
		String URL = scanner.next();
		Crawler mailcrawl = new Crawler();
		mailcrawl.search(URL, "2014");

	}

}
