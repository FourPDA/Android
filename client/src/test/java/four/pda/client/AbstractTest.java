package four.pda.client;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import four.pda.client.model.ListArticle;

/**
 * Created by swap_i on 25/10/15.
 */
public abstract class AbstractTest {

	protected static final String BASE_URL = "http://4pda.ru";
	private static final SimpleDateFormat ARTICLE_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

	protected String getHtmlSource(String urlPath) throws IOException {
		return IOUtils.toString(new URL(BASE_URL + urlPath + "?" + Math.random()), "cp1251");
	}

	protected void checkArticles(String pageSource) throws IOException {
		List<ListArticle> articles = new ArticleListParser().parse(pageSource);
		for (ListArticle article : articles) {
			String source = getHtmlSource(getArticleUrl(article.getDate(), article.getId()));
			new ArticlePageParser().parse(source);
		}
	}

	private String getArticleUrl(Date date, long id) {
		return "/" + ARTICLE_DATE_FORMAT.format(date) + "/" + id;
	}

}
