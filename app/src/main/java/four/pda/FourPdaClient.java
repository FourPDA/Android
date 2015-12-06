package four.pda;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.androidannotations.annotations.EBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import four.pda.client.ArticleListParser;
import four.pda.client.ArticlePageParser;
import four.pda.client.CommentTreeParser;
import four.pda.client.ReviewListParser;
import four.pda.client.model.AbstractComment;
import four.pda.client.model.ListArticle;
import four.pda.ui.CategoryType;

/**
 * Created by asavinova on 09/04/15.
 */
@EBean(scope = EBean.Scope.Singleton)
public class FourPdaClient {

	private static final Logger L = LoggerFactory.getLogger(FourPdaClient.class);

	public static final SimpleDateFormat ARTICLE_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

	private static final String BASE_URL = "http://4pda.ru/";
	private OkHttpClient client = new OkHttpClient();

	public List<ListArticle> getArticles(CategoryType type, int page) throws IOException {
		String category = "";
		if (type != CategoryType.ALL) {
			category = type.name().toLowerCase() + "/";
		}

		String url = BASE_URL + category + "page/" + page;

		Request request = new Request.Builder()
				.url(url)
				.build();

		Response response = client.newCall(request).execute();
		String body = response.body().string();

		try {
			if (type == CategoryType.REVIEWS) {
                return new ReviewListParser().parse(body);
            } else {
                return new ArticleListParser().parse(body);
            }
		} catch (RuntimeException e) {
			L.error("Can't parse page at " + url);
			throw e;
		}
	}

	public String getArticleContent(Date date, long id) throws IOException {
		String fullId = ARTICLE_DATE_FORMAT.format(date) + "/" + id;
		String url = BASE_URL + fullId;
		Request request = new Request.Builder()
				.url(url)
				.build();

		Response response = client.newCall(request).execute();
		String body = response.body().string();

		try {
			return new ArticlePageParser().parse(body);
		} catch (RuntimeException e) {
			L.error("Can't parse page at " + url);
			throw e;
		}
	}

	public List<AbstractComment> getArticleComments(Date date, Long id) throws IOException {
		String fullId = ARTICLE_DATE_FORMAT.format(date) + "/" + id;
		String url = BASE_URL + fullId;
		Request request = new Request.Builder()
				.url(url)
				.build();

		Response response = client.newCall(request).execute();
		String body = response.body().string();

		try {
			return new CommentTreeParser().parse(body);
		} catch (RuntimeException e) {
			L.error("Can't parse comments at " + url);
			throw e;
		}
	}
}
