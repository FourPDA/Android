package ru4pda.news.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru4pda.news.client.model.ListArticle;

/**
 * Created by asavinova on 13/04/15.
 */
public class ReviewListParser {

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

	private static final Pattern REVIEWS_LIST_PATTERN = Pattern.compile("<ul class=\"product-list\">(.*?)<ul class=\"page-nav\">", Pattern.DOTALL);
	private static final Pattern REVIEW_PATTERN = Pattern.compile("itemtype=\"http://schema.org/Product\">.*?</div>\n\t\t</li>", Pattern.DOTALL);

	private static final Pattern URL_PATTERN = Pattern.compile("<a itemprop=\"url\" href=\"/(.*?)/\">", Pattern.DOTALL);
	private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("<div class=\"content\">(.*?)<span class=\"bg-shadow\">", Pattern.DOTALL);
	private static final Pattern TITLE_PATTERN = Pattern.compile("<div class=\"btn-review-all\">.*title=\"(.*?)\">полный обзор</a>", Pattern.DOTALL);
	private static final Pattern IMAGE_PATTERN = Pattern.compile("<img itemprop=\"image\" src=\"(.*?)\"", Pattern.DOTALL);

	public List<ListArticle> parse(String pageSource) {
		List<ListArticle> articles = new ArrayList<>();

		Matcher allArticlesMatcher = REVIEWS_LIST_PATTERN.matcher(pageSource);

		if (!allArticlesMatcher.find()) {
			throw new IllegalStateException("Can't find reviews list block");
		}
		String allArticlesSource = allArticlesMatcher.group(1);

		Matcher articleMatcher = REVIEW_PATTERN.matcher(allArticlesSource);

		while (articleMatcher.find()) {
            String itemSource = articleMatcher.group();
            articles.add(parseArticle(itemSource));
        }

		return articles;
	}

	private ListArticle parseArticle(String itemSource) {
		ListArticle article = new ListArticle();

		Matcher urlMatcher = URL_PATTERN.matcher(itemSource);
		if (!urlMatcher.find()) {
			throw new IllegalStateException("Can't find articles list item url");
		}
		String url = urlMatcher.group(1);
		int lastSlashIndex = url.lastIndexOf("/");

		article.setId(Long.parseLong(url.substring(lastSlashIndex + 1)));

		String dateString = url.substring(0, lastSlashIndex);
		try {
			article.setDate(DATE_FORMAT.parse(dateString));
        } catch (ParseException e) {
			throw new IllegalStateException("Can't parse date " + dateString);
        }

		Matcher titleMatcher = TITLE_PATTERN.matcher(itemSource);
		if (!titleMatcher.find()) {
			throw new IllegalStateException("Can't find articles list item title");
		}
		String title = titleMatcher.group(1);
		article.setTitle(title);

		Matcher descriptionMatcher = DESCRIPTION_PATTERN.matcher(itemSource);
		if (!descriptionMatcher.find()) {
			throw new IllegalStateException("Can't find articles list item description");
		}
		String description = descriptionMatcher.group(1);
		article.setDescription(description);

		Matcher imageMatcher = IMAGE_PATTERN.matcher(itemSource);
		if (!imageMatcher.find()) {
			throw new IllegalStateException("Can't find articles list item image");
		}
		String image = imageMatcher.group(1);
		article.setImage(image);

		return article;
	}
}
