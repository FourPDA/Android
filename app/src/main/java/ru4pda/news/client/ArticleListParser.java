package ru4pda.news.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru4pda.news.client.model.ListArticle;

/**
 * Created by asavinova on 09/04/15.
 */
public class ArticleListParser {

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

	private static final Pattern ARTICLE_LIST_PATTERN = Pattern.compile("<article id=\"content\" class=\"fix-post\">(.*?)<ul class=\"page-nav\">", Pattern.DOTALL);
	private static final Pattern ARTICLE_PATTERN = Pattern.compile("http://schema.org/Article\">.*?</article>", Pattern.DOTALL);

	private static final Pattern URL_PATTERN = Pattern.compile("<div class=\"visual\">.*?<a href=\"/(.*?)/\"", Pattern.DOTALL);
	private static final Pattern DESCRIPTION_BLOCK_PATTERN = Pattern.compile("<div class=\"description\">(.*?)<span class=\"bg-shadow\">", Pattern.DOTALL);
	private static final Pattern TITLE_PATTERN = Pattern.compile("itemprop=\"name\">(.*?)</a></h1>", Pattern.DOTALL);
	private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("<div itemprop=\"description\">(.*?)</div>", Pattern.DOTALL);
	private static final Pattern IMAGE_PATTERN = Pattern.compile("<img itemprop=\"image\" src=\"(.*?)\"", Pattern.DOTALL);

	public List<ListArticle> parse(String pageSource) {
		List<ListArticle> articles = new ArrayList<>();

		Matcher allArticlesMatcher = ARTICLE_LIST_PATTERN.matcher(pageSource);

		if (!allArticlesMatcher.find()) {
			throw new IllegalStateException("Can't find articles list block");
		}
		String allArticlesSource = allArticlesMatcher.group(1);

		Matcher articleMatcher = ARTICLE_PATTERN.matcher(allArticlesSource);

		while (articleMatcher.find()) {
            String itemSource = articleMatcher.group();
            articles.add(parseListItem(itemSource));
        }

		return articles;
	}

	private ListArticle parseListItem(String itemSource) {
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

		Matcher descBlockMatcher = DESCRIPTION_BLOCK_PATTERN.matcher(itemSource);
		if (!descBlockMatcher.find()) {
			throw new IllegalStateException("Can't find articles list item desctiption block");
		}
		String descriptionBlock = descBlockMatcher.group(1);

		{
            Matcher matcher = TITLE_PATTERN.matcher(descriptionBlock);
            if (!matcher.find()) {
                throw new IllegalStateException("Can't find articles list item title");
            }
            String title = matcher.group(1);
            article.setTitle(title);
        }

		{
            Matcher matcher = DESCRIPTION_PATTERN.matcher(descriptionBlock);
            if (!matcher.find()) {
                throw new IllegalStateException("Can't find articles list item description");
            }
            String description = matcher.group(1);
            article.setDescription(description);
        }

		Matcher imageMatcher = IMAGE_PATTERN.matcher(itemSource);
		if (!imageMatcher.find()) {
			throw new IllegalStateException("Can't find articles list item image");
		}

		String image = imageMatcher.group(1);
		article.setImage(image);

		return article;
	}
}
