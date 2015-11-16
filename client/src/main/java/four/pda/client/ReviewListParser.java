package four.pda.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import four.pda.client.model.ListArticle;

/**
 * Created by asavinova on 13/04/15.
 */
public class ReviewListParser extends AbstractParser {

	public List<ListArticle> parse(String pageSource) {

		Document document = Jsoup.parse(pageSource);
		Elements elements = document.select("li[itemtype=http://schema.org/CreativeWork][itemscope]");

		List<ListArticle> articles = new ArrayList<>();
		for (Element element : elements) {
			articles.add(articleFromElement(element));
		}
		return articles;
	}

	private ListArticle articleFromElement(Element element) {

		ListArticle article = new ListArticle();

		String url = element.select("a[itemprop=url]").attr("href");
		IdAndDate idAndDate = getIdAndDateFromUrl(url);
		article.setId(idAndDate.id);
		article.setDate(idAndDate.date);

		String title = element.select("span[itemprop=name]").get(0).text();
		article.setTitle(title);

		Element content = element.select("div.content").first();
		content.select("h1").remove();
		String description = content.html();
		article.setDescription(description);

		String image = element.select("img[itemprop=image]").attr("src");
		article.setImage(image);

		return article;
	}

}
