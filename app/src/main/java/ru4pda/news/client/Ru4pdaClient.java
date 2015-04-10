package ru4pda.news.client;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.androidannotations.annotations.EBean;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import ru4pda.news.client.model.FullArticle;
import ru4pda.news.client.model.SimpleArticle;

/**
 * Created by asavinova on 09/04/15.
 */
@EBean(scope = EBean.Scope.Singleton)
public class Ru4pdaClient {

	public static final SimpleDateFormat ARTICLE_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

	private static final String BASE_URL = "http://4pda.ru/";
	private OkHttpClient client = new OkHttpClient();

	public List<SimpleArticle> getArticles(int page) throws IOException {
		Request request = new Request.Builder()
				.url(BASE_URL + "/page/" + page)
				.build();

		Response response = client.newCall(request).execute();
		String body = response.body().string();
		return new HomePageParser().parse(body);
	}

	public FullArticle getContentArticle(SimpleArticle article) throws IOException {
		String fullId = ARTICLE_DATE_FORMAT.format(article.getDate())
				+ "/" + article.getId();
		Request request = new Request.Builder()
				.url(BASE_URL + fullId)
				.build();

		Response response = client.newCall(request).execute();
		String body = response.body().string();

		FullArticle fullArticle = new ArticlePageParser().parse(body);
		fullArticle.setSimpleArticle(article);
		return fullArticle;
	}
}
