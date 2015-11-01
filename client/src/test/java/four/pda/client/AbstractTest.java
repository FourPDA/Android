package four.pda.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by swap_i on 25/10/15.
 */
public abstract class AbstractTest {

	private static final String BASE_URL = "http://4pda.ru/";
	public static final SimpleDateFormat ARTICLE_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

	protected String getHtmlSource(String urlPath) throws IOException {
		URL url = new URL(urlPath);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		InputStream inputStream = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder builder = new StringBuilder();
		String nextLine = "";
		while ((nextLine = reader.readLine()) != null) {
			builder.append(nextLine);
		}
		return builder.toString();
	}

	protected String getArticleUrl(Date date, long id) {
		String fullId = ARTICLE_DATE_FORMAT.format(date) + "/" + id;
		return BASE_URL + fullId;
	}

}
