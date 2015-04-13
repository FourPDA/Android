package ru4pda.news.client;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.androidannotations.annotations.EBean;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ru4pda.news.client.model.ListArticle;
import ru4pda.news.ui.CategoryType;

/**
 * Created by asavinova on 09/04/15.
 */
@EBean(scope = EBean.Scope.Singleton)
public class Ru4pdaClient {

	public static final SimpleDateFormat ARTICLE_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

	private static final String BASE_URL = "http://4pda.ru/";
	private OkHttpClient client = new OkHttpClient();

	public List<ListArticle> getArticles(CategoryType type, int page) throws IOException {
		String category = "";
		if (type != CategoryType.ALL) {
			category = type.name().toLowerCase() + "/";
		}

		Request request = new Request.Builder()
				.url(BASE_URL + category + "page/" + page)
				.build();

		Response response = client.newCall(request).execute();
		String body = response.body().string();

		if (type == CategoryType.REVIEWS) {
			return new ReviewsParser().parse(body);
		} else {
			return new ArticleListParser().parse(body);
		}
	}

	public String getArticleContent(Date date, long id) throws IOException {
		String fullId = ARTICLE_DATE_FORMAT.format(date) + "/" + id;
		Request request = new Request.Builder()
				.url(BASE_URL + fullId)
				.build();

		Response response = client.newCall(request).execute();
		String body = response.body().string();

		return new ArticlePageParser().parse(body);
	}

}
