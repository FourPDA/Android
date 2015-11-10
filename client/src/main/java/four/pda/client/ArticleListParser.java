package four.pda.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import four.pda.client.model.ListArticle;

/**
 * Created by asavinova on 09/04/15.
 */
public class ArticleListParser extends AbstractParser {

	public List<ListArticle> parse(String pageSource) {

		Document document = Jsoup.parse(pageSource);
		Elements elements = document.select("article [itemtype=http://schema.org/Article]");

		List<ListArticle> articles = new ArrayList<>();
		for (Element element : elements) {
			ListArticle article = parseListItem(element);
			if (article == null) {
				continue;
			}
			articles.add(article);
		}

		return articles;
	}

	private ListArticle parseListItem(Element element) {

		if (element.attr("data-ztm").isEmpty()) {
			// Не парсим рекламу
			return null;
		}

		ListArticle article = new ListArticle();

		String url = element.select("a[itemprop=url]").first().attr("href");

		if (url.contains("/special/")) {
			// Не парсим специальные ссылки
			// TODO Парсить спец. ссылки в отдельную модель
			return null;
		}

		IdAndDate idAndDate = getIdAndDateFromUrl(url);
		article.setId(idAndDate.id);
		article.setDate(idAndDate.date);

		Elements descrEl = element.select("div.description");

		article.setTitle(descrEl.select("h1 > a").text());

		descrEl.select("h1").remove();

		article.setDescription(descrEl.html());

		String imageSrc = element.select("img[itemprop=image]").first().attr("src");
		article.setImage(imageSrc);

		return article;
	}

}
