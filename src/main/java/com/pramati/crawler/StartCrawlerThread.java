package com.pramati.crawler;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

public class StartCrawlerThread {
	static BlockingQueue<String> urlQueue = new LinkedBlockingQueue<String>();
	static BlockingQueue<String> downloadQueue = new LinkedBlockingQueue<String>();
	static Set<String> visitedQueue = new HashSet<String>();
	final static Logger logger = Logger.getLogger(StartCrawlerThread.class);
	public static void main(String args[]) {
		try {
			logger.debug("Enter the URl to Parse");
			Scanner scanner = new Scanner(System.in);
			String URL = scanner.next();
			urlQueue.put(URL);
			//logger.debug("url size" + urlQueue.size());
			ExecutorService executor = Executors.newFixedThreadPool(10);
			//ExecutorService downexecutor = Executors.newFixedThreadPool(5);
			for (int i = 0; i < 10; i++) {
				Runnable worker = new CrawlerThread(urlQueue, visitedQueue,downloadQueue);
				Runnable downloaderWorker = new DownloadFileThread(downloadQueue);
				executor.execute(worker);
				executor.execute(downloaderWorker);
			}
			
			//logger.debug("url size" + urlQueue.size());
			
			
			executor.shutdown();
			
			
			while (!executor.isTerminated()) {
				
			}
			//DownloadFileThread.setFinishedcrawler(true);	
			//while (!downexecutor.isTerminated()) {
			//}
			System.out.println("Finished all threads");
			scanner.close();
		} catch (Exception e) {
			logger.error("Exception in main", e);
		}
	}

}
