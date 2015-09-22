package com.pramati.crawler;
import java.io.*;
import java.net.URL;

public class DownloadFile implements Download {

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
				System.out.println("Cannot download file : " + format+e);
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
				System.out.println("File " + format
						+ " downloaded successfully");
			}
		}
	}

}
