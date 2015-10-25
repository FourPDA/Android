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

	@Test
	public void parse() throws IOException {
		String htmlFile = "/html/news/reviews.html";
		String pageSource = getTestFile(htmlFile);
		List<ListArticle> articles = new ReviewListParser().parse(pageSource);
		Assert.assertEquals("Reviews items on page not 30", 30, articles.size());
	}

}
