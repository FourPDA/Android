package four.pda.client;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import four.pda.client.model.ListArticle;
import four.pda.client.parsers.ArticleListParser;

/**
 * Created by asavinova on 19/01/16.
 */
public class DatePublishedParserTest extends AbstractTest {

	@Test
	public void fivePages() throws IOException {

		Date prevDate;
		Date nextDate = new Date();

		for (int i = 1; i <= 5; i++) {
			String pageSource = getHtmlSource("/page/" + i);
			List<ListArticle> articles = new ArticleListParser().parse(pageSource);

			for (ListArticle article : articles) {
				prevDate = nextDate;
				nextDate = article.getPublishedDate();

				Assert.assertTrue("Wrong dates published", !prevDate.before(nextDate));
			}
		}

	}

}
