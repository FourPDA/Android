package four.pda.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import four.pda.client.model.Captcha;

/**
 * Created by asavinova on 08/02/16.
 */
public class CaptchaParser {

	public Captcha parse(String pageSource) {

		Captcha captcha = new Captcha();

		Document document = Jsoup.parse(pageSource);
		Element divElement = document.select("div.captcha").first();

		Element time = divElement.select("input[name=captcha-time]").first();
		captcha.setTime(time.attr("value"));

		Element sig = divElement.select("input[name=captcha-sig]").first();
		captcha.setSig(sig.attr("value"));

		Element img = divElement.select("img").first();
		captcha.setUrl(img.attr("src"));

		return captcha;
	}

}
