package four.pda.dagger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import four.pda.client.CategoryType;
import four.pda.client.FourPdaClient;
import four.pda.client.exceptions.ParseException;
import four.pda.client.model.ListArticle;
import okhttp3.OkHttpClient;

/**
 * Created by asavinova on 23/02/16.
 */
public class DummyFourPdaClient extends FourPdaClient {

	private static List<ListArticle> articles = new ArrayList<>();

	static {
		{
			ListArticle article = new ListArticle();
			article.setId(0);
			article.setTitle("Обычное поведение");
			article.setDescription("");
			article.setImage("http://s.4pda.to/Af3e6vSAQSlqjknILz0pcMd4igtNNtXXYZLvV.jpg");
			article.setDate(new Date());
			article.setPublishedDate(new Date());
			articles.add(article);
		}
		{
			ListArticle article = new ListArticle();
			article.setId(1);
			article.setTitle("Ошибка парсинга");
			article.setDescription("");
			article.setImage("http://s.4pda.to/Af3e6vSAQSlqjknILz0pcMd4igtNNtXXYZLvV.jpg");
			article.setDate(new Date());
			article.setPublishedDate(new Date());
			articles.add(article);
		}
		{
			ListArticle article = new ListArticle();
			article.setId(2);
			article.setTitle("Ошибка сети");
			article.setDescription("");
			article.setImage("http://s.4pda.to/Af3e6vSAQSlqjknILz0pcMd4igtNNtXXYZLvV.jpg");
			article.setDate(new Date());
			article.setPublishedDate(new Date());
			articles.add(article);
		}
	}


	public DummyFourPdaClient(OkHttpClient client) {
		super(client);
	}

	@Override
	public List<ListArticle> getArticles(CategoryType type, int page) throws IOException {
		return articles;
	}

	@Override
	public String getArticleContent(Date date, long id) throws IOException {
		if (id == 0) {
			return articles.get(0).getTitle();
		}

		if (id == 1) {
			throw new ParseException("");
		}

		throw new IOException();
	}

}
