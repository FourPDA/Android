package four.pda.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import four.pda.client.model.ListArticle;

/**
 * Created by asavinova on 13/04/15.
 */
public class ReviewListParser {

	public List<ListArticle> parse(String pageSource) {

		Document document = Jsoup.parse(pageSource);
		Elements elements = document.select("ul.product-list > li[itemscope]");

		List<ListArticle> articles = new ArrayList<>();
		for (Element element : elements) {
			articles.add(articleFromElement(element));
		}
		return articles;
	}

	private ListArticle articleFromElement(Element element) {

		ListArticle article = new ListArticle();

		String url = element.select("a[itemprop=url]").attr("href");
		String[] urlParts = url.split("/");

		long id = Long.parseLong(urlParts[4]);
		article.setId(id);

		Calendar calendar = Calendar.getInstance();
		int year = Integer.parseInt(urlParts[1]);
		int month = Integer.parseInt(urlParts[2]);
		int date = Integer.parseInt(urlParts[3]);
		calendar.set(year, month, date, 0, 0, 0);
		article.setDate(calendar.getTime());

		String title = element.select("a[itemprop=name]").get(0).text();
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
