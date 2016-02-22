package four.pda.client;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import four.pda.client.model.ListArticle;
import four.pda.client.parsers.ArticleListParser;

/**
 * Created by swap_i on 25/10/15.
 */
public class ArticleListParserTest extends AbstractTest {

	@Test
	public void mainPage() throws IOException {
		String pageSource = getHtmlSource("/");
		List<ListArticle> articles = new ArticleListParser().parse(pageSource);
		Assert.assertEquals("Wrong articles list size", 30, articles.size());
	}

	@Test
	public void checkContentArticles() throws IOException {
		checkArticles(getHtmlSource("/"));
		checkArticles(getHtmlSource("/page/2/"));
	}

}
