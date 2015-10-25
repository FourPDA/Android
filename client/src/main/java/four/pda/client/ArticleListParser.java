package four.pda.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import four.pda.client.model.ListArticle;

/**
 * Created by asavinova on 09/04/15.
 */
public class ArticleListParser extends AbstractParser {

	public List<ListArticle> parse(String pageSource) {

		Document document = Jsoup.parse(pageSource);
		Elements elements = document.select("article#content > article.post");

		List<ListArticle> articles = new ArrayList<>();
		for (Element element : elements) {
			articles.add(parseListItem(element));
		}

		return articles;
	}

	private ListArticle parseListItem(Element element) {

		ListArticle article = new ListArticle();

		IdAndDate idAndDate = getIdAndDateFromUrl(element.select("a[itemprop=url]").first().attr("href"));
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
