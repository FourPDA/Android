package four.pda.client;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import four.pda.client.model.ListArticle;
import four.pda.client.parsers.ReviewListParser;

/**
 * Created by swap_i on 25/10/15.
 */
public class ReviewsListParserTest extends AbstractTest {

	private static final String REVIEW_PATH = "/reviews";

	@Test
	public void mainPage() throws IOException {
		String pageSource = getHtmlSource(REVIEW_PATH);
		List<ListArticle> articles = new ReviewListParser().parse(pageSource);
		Assert.assertEquals("Wrong reviews list size", 30, articles.size());
	}

	@Test
	public void checkContentArticles() throws IOException {
		checkArticles(getHtmlSource(REVIEW_PATH));
		checkArticles(getHtmlSource(REVIEW_PATH + "/page/2/"));
	}

}
