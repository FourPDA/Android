package four.pda.client;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by asavinova on 01/11/15.
 */
public class ArticleContentParserTest extends AbstractTest {

	@Test
	public void simplePage() throws IOException {
		String mainPage = "/2015/11/01/254823/";
		String pageSource = getHtmlSource(mainPage);
		new ArticlePageParser().parse(pageSource);
	}

	@Test
	public void pageWithVideo() throws IOException {
		String page = "/2015/11/01/254700/";
		String pageSource = getHtmlSource(page);
		new ArticlePageParser().parse(pageSource);
	}

	@Test
	public void reviewPage() throws IOException {
		String page = "/2015/10/31/254594/";
		String pageSource = getHtmlSource(page);
		new ArticlePageParser().parse(pageSource);
	}

}
