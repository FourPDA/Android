package ru4pda.news.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asavinova on 09/04/15.
 */
public class ArticlePageParser {

	private static final Pattern CONTENT_PATTERN = Pattern.compile("width=\"1\" height=\"1\" /><p.*?style=\"text-align: justify.*?>(.*?)<br /></div>", Pattern.DOTALL);

	public String parse(String pageSource) {

		Matcher matcher = CONTENT_PATTERN.matcher(pageSource);

		if (!matcher.find()) {
			throw new IllegalStateException("Can't parse page");
		}

		return matcher.group(1);
	}

}
