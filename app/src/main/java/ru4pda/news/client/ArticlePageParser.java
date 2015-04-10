package ru4pda.news.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru4pda.news.client.model.FullArticle;

/**
 * Created by asavinova on 09/04/15.
 */
public class ArticlePageParser {

	private static final Pattern CONTENT_PATTERN = Pattern.compile("<div class=\"content\">(.*?)<div class=\"materials-box\">", Pattern.DOTALL);

	public FullArticle parse(String pageSource) {
		FullArticle article = new FullArticle();

		Matcher matcher = CONTENT_PATTERN.matcher(pageSource);

		if (matcher.find()) {
			article.setContent(matcher.group(1));
		} else {
			//TODO
		}

		return article;
	}

}
