package com.pramati.crawler;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;

public class StartCrawlerThread {
	BlockingQueue<String> urlQueue = new LinkedBlockingQueue<String>();
	BlockingQueue<String> visitedQueue = new LinkedBlockingQueue<String>();
	final static Logger logger = Logger.getLogger(StartCrawler.class);

	public static void main(String args[]) {
		try
		{
		logger.debug("Enter the URl to Parse");
		Scanner scanner = new Scanner(System.in);
		String URL = scanner.next();
		StartCrawlerThread s = new StartCrawlerThread();
		s.urlQueue.put(URL);
		ExecutorService executor = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 5; i++) {
			Runnable worker = new CrawlerThread(s.urlQueue,s.visitedQueue);
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}

		System.out.println("Finished all threads");
		scanner.close();
	}
		catch(Exception e)
		{
			logger.error("Exception in main",e);
		}
	}	
	
}
