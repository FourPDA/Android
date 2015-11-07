package four.pda.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import four.pda.client.model.Comment;

/**
 * Created by asavinova on 02/11/15.
 */
public class CommentTreeParser extends AbstractParser {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy | HH:ss");

	public List<Comment> parse(String pageSource) {

		Document document = Jsoup.parse(pageSource);
		Element element = document.select("div#comments").first();
		return parseList(element, 0);
	}

	private List<Comment> parseList(Element rootElement, int level) {
		Elements elements = rootElement.select(".comment-list > li");
		List<Comment> comments = new ArrayList<>();
		for (Element element : elements) {
			Comment comment = parseComment(element, level);
			if (comment == null) {
				continue;
			}
			comments.add(comment);
		}

		return comments;
	}

	private Comment parseComment(Element element, int level) {

		Comment comment = new Comment();

		Element karmaElement = element.select(".karma").first();
		String idString = karmaElement.attr("data-karma");
		String[] split = idString.split("-");
		if (split.length > 1) {
			idString = split[1];
			comment.setId(Long.parseLong(idString));
		} else {
			//TODO
			return null;
		}

		String nickname = element.select(".nickname").first().text();
		comment.setNickname(nickname);

		String metaString = element.select(".h-meta").first().text();
		metaString = metaString.trim();
		try {
			comment.setDate(DATE_FORMAT.parse(metaString));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

		String content = element.select(".content").first().text();
		comment.setContent(content);

		comment.setLevel(level);

		level++;
		comment.setCommentList(parseList(element, level));

		return comment;
	}

}
