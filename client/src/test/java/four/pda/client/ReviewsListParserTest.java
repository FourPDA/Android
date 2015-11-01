package four.pda.client;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import four.pda.client.model.ListArticle;

/**
 * Created by swap_i on 25/10/15.
 */
public class ReviewsListParserTest extends AbstractTest {

	private static final String REVIEW_BASE_URL = BASE_URL + "reviews/";

	@Test
	public void parseMainPage() throws IOException {
		String pageSource = getHtmlSource(REVIEW_BASE_URL);
		List<ListArticle> articles = new ReviewListParser().parse(pageSource);
		Assert.assertEquals("Wrong reviews list size", 30, articles.size());
	}

	@Test
	public void checkContentArticles() throws IOException {
		checkArticles(getHtmlSource(REVIEW_BASE_URL));
		checkArticles(getHtmlSource(REVIEW_BASE_URL + "page/2/"));
	}

}
