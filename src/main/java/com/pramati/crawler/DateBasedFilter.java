package com.pramati.crawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateBasedFilter implements IMailFilter {

	public Boolean evaluate(String regex, String content) {
		//String mailRegex="mailto:(.*)";
		String dateRegex="Date:(.*)"+regex;
		Pattern pattern = Pattern.compile(dateRegex);
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			return true;
		}
		return false;

	}

}