package four.pda.client.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import four.pda.client.model.Profile;

/**
 * Created by asavinova on 19/02/16.
 */
public class ProfileParser {

	public Profile parse(String pageSource) {

		Profile profile = new Profile();

		Document document = Jsoup.parse(pageSource);

		Element imgElement = document.select("div.photo > img").first();
		profile.setPhoto(imgElement.attr("src"));

		Element loginElement = document.select("div.user-box > h1").first();
		profile.setLogin(loginElement.text());

		return profile;
	}

}
