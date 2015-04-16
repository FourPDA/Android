package ru4pda.news.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asavinova on 09/04/15.
 */
public class ArticlePageParser {

	private static final String CONTENT_PATTERN_STRING = "<img src=\"/pages/imp/%s.gif\" width=\"1\" height=\"1\" />(.*?)<br /></div>";

	public String parse(long id, String pageSource) {

		String format = String.format(CONTENT_PATTERN_STRING, id);
		Pattern pattern = Pattern.compile(format, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(pageSource);

		if (!matcher.find()) {
			throw new IllegalStateException("Can't parse page");
		}

		return matcher.group(1);
	}

}
