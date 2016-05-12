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
	public void android() throws IOException {
		String pageSource = getHtmlSource("/?s=android");
		SearchContainer container = new SearchArticlesParser().parse(pageSource);
		Assert.assertTrue("Wrong search 'android' result", container.getCount() > 0);
	}

	@Test
	public void androidSecondPage() throws IOException {
		String pageSource = getHtmlSource("/page/2/?s=android");
		SearchContainer container = new SearchArticlesParser().parse(pageSource);
		Assert.assertTrue("Wrong search 'android' result from second page", container.getCount() > 0);
	}

	@Test
	public void emptyResult() throws IOException {
		String pageSource = getHtmlSource("/?s=qweasdzxc");
		SearchContainer container = new SearchArticlesParser().parse(pageSource);
		Assert.assertTrue("Wrong search 'qweasdzxc' result", container.getCount() == 0);
	}

	@Test
	public void parseException() throws IOException {
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
