package ru4pda.news.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru4pda.news.parser.model.SimpleArticle;

/**
 * Created by asavinova on 09/04/15.
 */
public class HomePageParser {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd");

	private static final Pattern ARTICLE_LIST_PATTERN = Pattern.compile("<article id=\"content\" class=\"fix-post\">(.*?)<ul class=\"page-nav\">", Pattern.DOTALL);
	private static final Pattern ARTICLE_PATTERN = Pattern.compile("http://schema.org/Article\">.*?</article>", Pattern.DOTALL);

	private static final Pattern URL_PATTERN = Pattern.compile("<div class=\"visual\">.*?<a href=\"/(.*?)/\"", Pattern.DOTALL);
	private static final Pattern DESCRIPTION_BLOCK_PATTERN = Pattern.compile("<div class=\"description\">(.*?)<span class=\"bg-shadow\">", Pattern.DOTALL);
	private static final Pattern TITLE_PATTERN = Pattern.compile("itemprop=\"name\">(.*?)</a></h1>", Pattern.DOTALL);
	private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("<div itemprop=\"description\"><p.*?>(.*?)</p></div>", Pattern.DOTALL);

	public List<SimpleArticle> parse(String pageSource) {
		List<SimpleArticle> articles = new ArrayList<>();

		Matcher allArticlesMatcher = ARTICLE_LIST_PATTERN.matcher(pageSource);

		if (!allArticlesMatcher.find()) {
			//TODO
		}

		String allArticlesSource = allArticlesMatcher.group(1);

		Matcher articleMatcher = ARTICLE_PATTERN.matcher(allArticlesSource);

		while (articleMatcher.find()) {
			String itemSource = articleMatcher.group();
			articles.add(parseArticle(itemSource));
		}

		return articles;
	}

	private SimpleArticle parseArticle(String itemSource) {
		SimpleArticle article = new SimpleArticle();

		Matcher urlMatcher = URL_PATTERN.matcher(itemSource);
		if (urlMatcher.find()) {
			String url = urlMatcher.group(1);
			int lastSlashIndex = url.lastIndexOf("/");

			article.setId(Long.parseLong(url.substring(lastSlashIndex + 1)));

			try {
				article.setDate(dateFormat.parse(url.substring(0, lastSlashIndex)));
			} catch (ParseException e) {
				e.printStackTrace();
			}

		} else {
			//TODO
		}

		Matcher descBlockMatcher = DESCRIPTION_BLOCK_PATTERN.matcher(itemSource);
		if (descBlockMatcher.find()) {

			String descriptionBlock = descBlockMatcher.group(1);

			{
				Matcher matcher = TITLE_PATTERN.matcher(descriptionBlock);
				if (matcher.find()) {
					String title = matcher.group(1);
					article.setTitle(title);
				} else {
					//TODO
				}
			}

			{
				Matcher matcher = DESCRIPTION_PATTERN.matcher(descriptionBlock);
				if (matcher.find()) {
					String description = matcher.group(1);
					article.setDescription(description);
				} else {
					//TODO
				}
			}

		} else {
			//TODO
		}

		return article;
	}
}
