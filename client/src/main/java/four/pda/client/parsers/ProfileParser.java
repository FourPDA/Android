package four.pda.client.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import four.pda.client.exceptions.ParseException;
import four.pda.client.model.Profile;

/**
 * Created by asavinova on 19/02/16.
 */
public class ProfileParser {

	private static final Logger L = LoggerFactory.getLogger(ProfileParser.class);

	public Profile parse(String pageSource) {

		Profile profile = new Profile();

		Document document = Jsoup.parse(pageSource);

		try {
			Element imgElement = document.select("div.photo > img").first();
			profile.setPhoto(imgElement.attr("src"));

			Element loginElement = document.select("div.user-box > h1").first();
			profile.setLogin(loginElement.text());
		} catch (Exception e) {
			String message = "Profile page parse exception";
			L.error(message, e);
			throw new ParseException(message);
		}

		return profile;
	}

}
