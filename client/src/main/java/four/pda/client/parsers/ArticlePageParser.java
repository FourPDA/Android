package four.pda.client.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asavinova on 09/04/15.
 */
public class ArticlePageParser {

	private static final String PREVIEW_URL_STRING = "http://i.ytimg.com/vi/%s/hqdefault.jpg";

	public String parse(String pageSource) {
		Document document = Jsoup.parse(pageSource);
		Element content = document.select("div.content").first();
		replaceVideoBlocks(content);
		return content.html();
	}

	private void replaceVideoBlocks(Element element) {

		for (Element videoBlockEl : element.select("iframe[allowfullscreen]")) {

			String src = videoBlockEl.attr("src");

			if (!src.contains("www.youtube.com")) {
				continue;
			}

			String hash = src.substring(src.lastIndexOf("/") + 1);
			String previewImageUrl = String.format(PREVIEW_URL_STRING, hash);
			String link = String.format("<a href=\"%s\"><img src=\"%s\" width=\"100%%\" /></a>", src, previewImageUrl);

			videoBlockEl.parent().append(link);
			videoBlockEl.remove();

		}

	}

}
