package four.pda.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import four.pda.client.model.AbstractComment;
import four.pda.client.model.Captcha;
import four.pda.client.model.ListArticle;
import four.pda.client.model.LoginResult;
import four.pda.client.parsers.ArticleListParser;
import four.pda.client.parsers.ArticlePageParser;
import four.pda.client.parsers.CaptchaParser;
import four.pda.client.parsers.CommentTreeParser;
import four.pda.client.parsers.LoginParser;
import four.pda.client.parsers.ReviewListParser;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by asavinova on 09/04/15.
 */
public class FourPdaClient {

	private static final Logger L = LoggerFactory.getLogger(FourPdaClient.class);

	public static final SimpleDateFormat ARTICLE_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

	private static final String BASE_URL = "http://4pda.ru/";

	private OkHttpClient client;

	public FourPdaClient(OkHttpClient client) {
		this.client = client;
	}

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
			L.error(String.format("Can't parse comments at %s",  url), e);
			throw e;
		}
	}

	public Captcha getCaptcha() throws IOException {
		String url = BASE_URL + "forum/index.php?act=login";
		Request request = new Request.Builder()
				.url(url)
				.build();

		Response response = client.newCall(request).execute();
		String body = response.body().string();

		try {
			return new CaptchaParser().parse(body);
		} catch (RuntimeException e) {
			L.error("Can't parse login page", e);
			throw e;
		}
	}

	public LoginResult login(LoginParams params) throws IOException {
		String url = BASE_URL + "forum/index.php?act=auth";

		RequestBody requestBody = new FormBody.Builder()
				.add("login", params.getLogin())
				.add("password", params.getPassword())
				.add("captcha-time", params.getCaptchaTime())
				.add("captcha-sig", params.getCaptchaSig())
				.add("captcha", params.getCaptcha())
				.build();

		Request request = new Request.Builder()
				.post(requestBody)
				.url(url)
				.build();

		Response response = client.newCall(request).execute();

		try {
			return new LoginParser().parse(response.body().string());
		} catch (RuntimeException e) {
			L.error("Can't parse login result page", e);
			throw e;
		}
	}

	public class LoginParams {

		private String login;
		private String password;
		private String captchaTime;
		private String captchaSig;
		private String captcha;

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getCaptchaTime() {
			return captchaTime;
		}

		public void setCaptchaTime(String captchaTime) {
			this.captchaTime = captchaTime;
		}

		public String getCaptchaSig() {
			return captchaSig;
		}

		public void setCaptchaSig(String captchaSig) {
			this.captchaSig = captchaSig;
		}

		public String getCaptcha() {
			return captcha;
		}

		public void setCaptcha(String captcha) {
			this.captcha = captcha;
		}
	}

}
