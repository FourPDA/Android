package ru4pda.news.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru4pda.news.client.model.FullArticle;

/**
 * Created by asavinova on 09/04/15.
 */
public class ArticlePageParser {

	private static final Pattern CONTENT_PATTERN = Pattern.compile("width=\"1\" height=\"1\" /><p.*?style=\"text-align: justify.*?>(.*?)<br /></div>", Pattern.DOTALL);

	public FullArticle parse(String pageSource) {
		FullArticle article = new FullArticle();

		Matcher matcher = CONTENT_PATTERN.matcher(pageSource);

		if (!matcher.find()) {
			throw new IllegalStateException("Can't parse page");
		}

		String content = matcher.group(1);
		article.setContent(content);

		return article;
	}

}
