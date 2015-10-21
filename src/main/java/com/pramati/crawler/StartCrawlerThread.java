package com.pramati.crawler;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class StartCrawlerThread {
	static BlockingQueue<String> urlQueue = new LinkedBlockingQueue<String>();
	static BlockingQueue<String> downloadQueue = new LinkedBlockingQueue<String>();
	static Set<String> visitedQueue = new HashSet<String>();
	final static Logger logger = Logger.getLogger(StartCrawlerThread.class);

	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);
		try {
			logger.debug("Enter the URl to Parse");
			String URL = scanner.next();
			urlQueue.put(URL);
			ExecutorService executor = Executors.newFixedThreadPool(10);
			ExecutorService downexecutor = Executors.newFixedThreadPool(10);
			
			//**Starting Crawler Threads**
			for (int i = 0; i < 10; i++) {
				Runnable worker = new CrawlerThread(urlQueue, visitedQueue,
						downloadQueue);
				executor.execute(worker);
			}
			// **Starting downloader threads**
			for (int i = 0; i < 10; i++) {

				Runnable downloaderWorker = new DownloadFileThread(
						downloadQueue);
				downexecutor.execute(downloaderWorker);
			}
			executor.shutdown();
			downexecutor.shutdown();
			logger.debug("All tasks submitted.");
			
			try {
				executor.awaitTermination(1, TimeUnit.DAYS);

			} catch (InterruptedException e) {
			}
			//** Setting the exiting condition for downloader threads**
			
			DownloadFileThread.setFinishedcrawler(true);
			logger.debug("All crawler threads completed.");
			try {
				downexecutor.awaitTermination(1, TimeUnit.DAYS);

			} catch (InterruptedException e) {
			}
			logger.debug("Finished all threads");
		} catch (Exception e) {
			logger.error("Exception in main", e);
		}
		finally
		{
			scanner.close();
		}
	}

}
