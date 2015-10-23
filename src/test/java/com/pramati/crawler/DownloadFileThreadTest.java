package com.pramati.crawler;

import static org.junit.Assert.*;

import java.util.concurrent.BlockingQueue;

import org.junit.Test;


public class DownloadFileThreadTest {
	protected static BlockingQueue<String> downloadedQueue = null;
	DownloadFileThread down=new DownloadFileThread(downloadedQueue);
	@Test
	public void testDownloadFileThread() {
		 assertEquals(downloadedQueue,down.downloadedQueue);
	}

	@Test
	public void testSetFinishedcrawler() {
		DownloadFileThread.setFinishedcrawler(true);
		assertEquals(true,DownloadFileThread.finishedcrawler);
	}

	@Test
	public void testRun() {
		fail("Not yet implemented");
	}

	@Test
	public void testDownloadMail() {
		fail("Not yet implemented");
	}

}
