package ru4pda.news.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asavinova on 09/04/15.
 */
public class ArticlePageParser {

	private static final Pattern CONTENT_PATTERN = Pattern.compile("<div class=\"content\">(.*?)<ul class=\"page-nav box\">", Pattern.DOTALL);
	private static final Pattern CLEAR_MATERIALS_BOX_PATTERN = Pattern.compile("(.*?)<div class=\"materials-box\">", Pattern.DOTALL);

	public String parse(String pageSource) {

		Matcher matcher = CONTENT_PATTERN.matcher(pageSource);

		if (matcher.find()) {
			String dirtyContent = matcher.group(1);

			Matcher materialsMatcher = CLEAR_MATERIALS_BOX_PATTERN.matcher(dirtyContent);
			if (materialsMatcher.find()) {
				return materialsMatcher.group(1);
			} else {
				return dirtyContent;
			}

		} else {
			//TODO
		}

		return "";
	}

}
