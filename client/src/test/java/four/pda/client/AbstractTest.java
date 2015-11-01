package four.pda.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by swap_i on 25/10/15.
 */
public abstract class AbstractTest {

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

}
