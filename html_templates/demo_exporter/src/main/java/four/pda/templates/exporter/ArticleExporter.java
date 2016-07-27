package four.pda.templates.exporter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;

import four.pda.client.parsers.ArticlePageParser;
import four.pda.template.NewsArticleTemplate;

/**
 * Created by pavel on 26/05/16.
 */
public class ArticleExporter {

	private static final Logger L = LoggerFactory.getLogger(ArticleExporter.class);

	private static final String SERVER = "http://4pda.ru/";

	public static void main(String[] args) {
		convertPage("2016/05/26/300085", "news_text");
		convertPage("2016/04/27/294201", "review");
	}

	private static void convertPage(String url, String filename) {
		try {
			String originalPage = IOUtils.toString(URI.create(SERVER + url), "CP1251");
			String croppedPage = new ArticlePageParser().parse(originalPage);
			String wrappedPage = new NewsArticleTemplate().make(croppedPage);
			FileUtils.write(new File(filename + ".htm"), wrappedPage);
		} catch (Exception e) {
			L.error("Can't convert page " + filename, e);
		}
	}

}
