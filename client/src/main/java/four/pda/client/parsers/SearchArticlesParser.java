package four.pda.client.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import four.pda.client.exceptions.ParseException;
import four.pda.client.model.ListArticle;
import four.pda.client.model.SearchContainer;

/**
 * Created by asavinova on 07/05/16.
 */
public class SearchArticlesParser extends AbstractParser {

	private static final Logger L = LoggerFactory.getLogger(SearchArticlesParser.class);

	public SearchContainer parse(String pageSource) {

		Document document = Jsoup.parse(pageSource);

		SearchContainer container = new SearchContainer();

		Element searchBoxElement = document.select(".search-box").first();

		if (searchBoxElement == null) {
			String message = "Can't find search element";
			L.error(message);
			throw new ParseException(message);
		}

		Element countElement = searchBoxElement.select(".content > .s-count").first();
		String countString = countElement.select("dd").first().text();
		container.setCount(Integer.parseInt(countString));

		Elements elements = document.select(".search-list > li");
		List<ListArticle> articles = new ArrayList<>();

		for (Element element : elements) {

			ListArticle article;
			try {
				article = parseListItem(element);
			} catch (Exception e) {
				String message = "Can't parse search articles list";
				L.error(message, e);
				throw new ParseException(message, e);
			}

			if (article == null) {
				continue;
			}
			articles.add(article);
		}

		container.setArticles(articles);

		return container;
	}

	private ListArticle parseListItem(Element element) {

		ListArticle article = new ListArticle();

		Element titleElement = element.select(".content > h1 > a").first();
		String url = titleElement.attr("href");

		if (url.contains("/special/")) {
			// Не парсим специальные ссылки
			// TODO Парсить спец. ссылки в отдельную модель
			return null;
		}

		if (url.contains("/tag/")) {
			// Не парсим статьи-тэги
			// TODO Парсить статьи-тэги отдельно
			return null;
		}

		IdAndDate idAndDate = getIdAndDateFromUrl(url);
		article.setId(idAndDate.id);
		article.setDate(idAndDate.date);

		article.setTitle(titleElement.text());
		article.setDescription(element.select(".content > p > a").first().text());

		String imageSrc = element.select(".photo > a > img").first().attr("src");
		article.setImage(imageSrc);

		return article;
	}

}
