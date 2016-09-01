package four.pda.client.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import four.pda.client.exceptions.ParseException;

/**
 * Created by asavinova on 09/04/15.
 */
public class ArticlePageParser {

	private static final Logger L = LoggerFactory.getLogger(ArticlePageParser.class);

	private static final String PREVIEW_URL_STRING = "http://i.ytimg.com/vi/%s/hqdefault.jpg";

	public String parse(String pageSource) {
		Document document = Jsoup.parse(pageSource);
		Element content = document.select("div.content").first();

		if (content == null) {
			String message = "Article content not found";
			L.error(message);
			throw new ParseException(message);
		}

		try {
			replaceVideoBlocks(content);
		} catch (Exception e) {
			String message = "Can't parse article page";
			L.error(message, e);
			throw new ParseException(message, e);
		}
		return content.html();
	}

	private void replaceVideoBlocks(Element element) {

		for (Element videoBlockEl : element.select("iframe[allowfullscreen]")) {

			String src = videoBlockEl.attr("src");

			if (!src.contains("www.youtube.com")) {
				continue;
			}

			String hash = src.substring(src.lastIndexOf("/") + 1, src.lastIndexOf("?"));
			String previewImageUrl = String.format(PREVIEW_URL_STRING, hash);
			String link = String.format("<a href=\"%s\"><img src=\"%s\" width=\"100%%\" /></a>", src, previewImageUrl);

			videoBlockEl.parent().append(link);
			videoBlockEl.remove();

		}

	}

}
