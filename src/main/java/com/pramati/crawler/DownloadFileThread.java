package com.pramati.crawler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public class DownloadFileThread implements Download, Runnable {
	protected static BlockingQueue<String> downloadedQueue = null;
	final static Logger logger = Logger.getLogger(DownloadFileThread.class);
	Properties configFile = new Properties();

	public DownloadFileThread(BlockingQueue<String> downloadQueue) {
		downloadedQueue = downloadQueue;
	}

	public void run() {
		logger.debug("Staring the downloader thread ...");
		try {
			String currentUrl = downloadedQueue.take();
			logger.debug("downloader Thread" + Thread.currentThread().getName());
			try {
				configFile.load(Crawler.class.getClassLoader()

				.getResourceAsStream("config.properties"));
				String dpath = configFile.getProperty("downloadPath");
				String libfile = dpath
						+ "2014"
						+ currentUrl.substring(currentUrl.indexOf("@") - 15,
								currentUrl.indexOf("@") - 3);
				downloadMail(currentUrl, libfile, ".txt");
			} catch (Exception e) {
				logger.error("Exception in opening properties file", e);
			}
		} catch (Exception e) {
			logger.error("Exception in getting the next URl", e);
		}
	}

	public void downloadMail(String url, String pathg, String format) {
		{
			BufferedInputStream in = null;
			FileOutputStream fout = null;
			try {
				in = new BufferedInputStream(new URL(url).openStream());
				File path = new File(pathg);
				fout = new FileOutputStream(path);

				byte data[] = new byte[1024];
				int count;
				while ((count = in.read(data, 0, 1024)) != -1) {
					fout.write(data, 0, count);
				}
			} catch (Exception e) {
				logger.error("Cannot download file : ", e);
			} finally {
				if (in != null)
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				if (fout != null)
					try {
						fout.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				logger.debug("File  for url " + url + pathg + format
						+ " downloaded successfully");
			}

		}
	}
}
