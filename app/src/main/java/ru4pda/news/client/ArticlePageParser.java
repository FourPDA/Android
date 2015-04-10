package ru4pda.news.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru4pda.news.client.model.FullArticle;

/**
 * Created by asavinova on 09/04/15.
 */
public class ArticlePageParser {

	private static final Pattern CONTENT_PATTERN = Pattern.compile("<div class=\"content\">(.*?)<ul class=\"page-nav box\">", Pattern.DOTALL);
	private static final Pattern CLEAR_MATERIALS_BOX_PATTERN = Pattern.compile("(.*?)<div class=\"materials-box\">", Pattern.DOTALL);

	public FullArticle parse(String pageSource) {
		FullArticle article = new FullArticle();

		Matcher matcher = CONTENT_PATTERN.matcher(pageSource);

		if (matcher.find()) {
			String dirtyContent = matcher.group(1);

			Matcher materialsMatcher = CLEAR_MATERIALS_BOX_PATTERN.matcher(dirtyContent);
			if (materialsMatcher.find()) {
				article.setContent(materialsMatcher.group(1));
			} else {
				article.setContent(dirtyContent);
			}

		} else {
			//TODO
		}

		return article;
	}

}
