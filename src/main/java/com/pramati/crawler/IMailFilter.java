package com.pramati.crawler;

public interface IMailFilter {

	public Boolean evaluate(String regex, String content);
}
