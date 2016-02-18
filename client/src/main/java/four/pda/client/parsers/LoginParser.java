package four.pda.client.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import four.pda.client.model.LoginResult;

/**
 * Created by asavinova on 16/02/16.
 */
public class LoginParser {

	public LoginResult parse(String pageSource) {

		LoginResult result = new LoginResult();

		Document document = Jsoup.parse(pageSource);
		Elements elements = document.select("ul.errors-list > li");

		if (elements == null || elements.size() == 0) {
			result.setResult(LoginResult.Result.OK);
			return result;
		}

		List<String> errors = new ArrayList<>();
		for (Element element : elements) {
			errors.add(element.text());
		}

		result.setResult(LoginResult.Result.ERROR);
		result.setErrors(errors);

		return result;
	}

}
