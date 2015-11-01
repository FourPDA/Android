package four.pda.client;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import four.pda.client.model.ListArticle;

/**
 * Created by swap_i on 25/10/15.
 */
public class ArticleListParserTest extends AbstractTest {

	@Test
	public void parseMainPage() throws IOException {
		String mainPage = "http://4pda.ru/";
		String pageSource = getHtmlSource(mainPage);
		List<ListArticle> articles = new ArticleListParser().parse(pageSource);
		Assert.assertEquals("Wrong articles list size", 30, articles.size());
	}

	@Test
	public void checkContentArticles() throws IOException {

		{
			String pageSource = getHtmlSource("http://4pda.ru/");
			List<ListArticle> articles = new ArticleListParser().parse(pageSource);
			for (ListArticle article : articles) {
				String source = getHtmlSource(getArticleUrl(article.getDate(), article.getId()));
				new ArticlePageParser().parse(source);
			}
		}

		{
			String pageSource = getHtmlSource("http://4pda.ru/page/2/");
			List<ListArticle> articles = new ArticleListParser().parse(pageSource);
			for (ListArticle article : articles) {
				String source = getHtmlSource(getArticleUrl(article.getDate(), article.getId()));
				new ArticlePageParser().parse(source);
			}
		}

	}

}
