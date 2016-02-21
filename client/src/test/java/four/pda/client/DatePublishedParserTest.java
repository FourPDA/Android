package four.pda.client;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import four.pda.client.model.ListArticle;

/**
 * Created by asavinova on 19/01/16.
 */
public class DatePublishedParserTest extends AbstractTest {

	private static final Logger L = LoggerFactory.getLogger(DatePublishedParserTest.class);

	@Test
	public void fivePages() throws IOException {

		Date prevDate;

		Date nextDate = new Date();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(nextDate);
		calendar.add(Calendar.YEAR, 1);
		nextDate = calendar.getTime();

		for (int i = 1; i <= 5; i++) {
			String pageSource = getHtmlSource("/page/" + i);
			List<ListArticle> articles = new ArticleListParser().parse(pageSource);

			for (ListArticle article : articles) {
				prevDate = nextDate;
				nextDate = article.getPublishedDate();

				if (prevDate.before(nextDate)) {
					L.debug(String.format("Next date %s before previous date %s", nextDate, prevDate));
				}

				Assert.assertTrue("Wrong dates published", !prevDate.before(nextDate));
			}
		}

	}

}
