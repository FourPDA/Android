package four.pda.client;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import four.pda.client.exceptions.ParseException;
import four.pda.client.model.SearchContainer;
import four.pda.client.parsers.SearchArticlesParser;

/**
 * Created by asavinova on 07/05/16.
 */
public class SearchArticlesTest extends AbstractTest {

	@Test
	public void searchCriteriaIsAndroid() throws IOException {
		String pageSource = getHtmlSource("/?s=android");
		SearchContainer container = new SearchArticlesParser().parse(pageSource);
		Assert.assertTrue("Wrong search 'android' result", container.getAllArticlesCount() > 0);
		Assert.assertTrue("Wrong current page for search 'android' result", container.getCurrentPage() == 1);
		Assert.assertTrue("Wrong next page for search 'android' result", container.hasNextPage());
	}

	@Test
	public void searchCriteriaIsAndroidSecondPage() throws IOException {
		String pageSource = getHtmlSource("/page/2/?s=android");
		SearchContainer container = new SearchArticlesParser().parse(pageSource);
		Assert.assertTrue("Wrong search 'android' result from second page", container.getAllArticlesCount() > 0);
		Assert.assertTrue("Wrong current page for search 'android' result", container.getCurrentPage() == 2);
		Assert.assertTrue("Wrong next page for search 'android' result", container.hasNextPage());
	}

	@Test
	public void searchCriteriaIsAndroidMaxPage() throws IOException {
		String pageSource = getHtmlSource("/page/334/?s=android");
		SearchContainer container = new SearchArticlesParser().parse(pageSource);
		Assert.assertFalse("Wrong next page for search 'android' result", container.hasNextPage());
		Assert.assertTrue("Wrong current page for search 'android' result", container.getCurrentPage() == 334);
	}

	@Test
	public void searchResultIsEmpty() throws IOException {
		String pageSource = getHtmlSource("/?s=qweasdzxc");
		SearchContainer container = new SearchArticlesParser().parse(pageSource);
		Assert.assertTrue("Wrong search 'qweasdzxc' result", container.getAllArticlesCount() == 0);
		Assert.assertFalse("Wrong next page for search 'qweasdzxc' result", container.hasNextPage());
		Assert.assertTrue("Wrong current page for search 'qweasdzxc' result", container.getCurrentPage() == 0);
	}

	@Test
	public void searchResultIsParseException() throws IOException {
		String pageSource = IOUtils.toString(new URL("http://www.google.ru/"), "utf-8");
		boolean isParserError = true;
		try {
			new SearchArticlesParser().parse(pageSource);
			isParserError = false;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Assert.assertTrue("Wrong result from http://www.google.ru/ page", isParserError);
	}

}
