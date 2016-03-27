package four.pda.client.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import four.pda.client.exceptions.ParseException;
import four.pda.client.model.AbstractComment;
import four.pda.client.model.Comment;
import four.pda.client.model.DeletedComment;

/**
 * Created by asavinova on 02/11/15.
 */
public class CommentTreeParser extends AbstractParser {

	private static final Logger L = LoggerFactory.getLogger(CommentTreeParser.class);

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy | HH:ss");

	public List<AbstractComment> parse(String pageSource) {
		Document document = Jsoup.parse(pageSource);
		Element element = document.select("div#comments").first();

		if (element == null) {
			String message = "Comments content not found";
			L.error(message);
			throw new ParseException(message);
		}

		try {
			return parseList(element, 0);
		} catch (Exception e) {
			String message = "Can't parse comments tree";
			L.error(message, e);
			throw new ParseException(message, e);
		}
	}

	private List<AbstractComment> parseList(Element rootElement, int level) {
		Element commentListEl = rootElement.select(".comment-list").first();
		List<AbstractComment> comments = new ArrayList<>();
		for (Element commentEl : commentListEl.children()) {
			AbstractComment comment = parseComment(commentEl, level);
			comments.add(comment);
		}
		return comments;
	}

	private AbstractComment parseComment(Element element, int level) {
		if (element.child(0).hasClass("deleted")) {
			return parseDeletedComment(element, level);
		} else {
			return parseNormalComment(element, level);
		}
	}

	private Comment parseNormalComment(Element element, int level) {

		Comment comment = new Comment();

		Element karmaElement = element.select(".karma").first();
		String idString = karmaElement.attr("data-karma");
		String[] split = idString.split("-");
		idString = split[1];
		comment.setId(Long.parseLong(idString));

		String nickname = element.select(".nickname").first().text();
		comment.setNickname(nickname);

		String metaString = element.select(".h-meta").first().text().trim();
		try {
			comment.setDate(DATE_FORMAT.parse(metaString));
		} catch (java.text.ParseException e) {
			String message = "Can't parse h-meta tag content as date";
			L.error(message, e);
			throw new RuntimeException(message, e);
		}

		String content = element.select(".content").first().html();
		comment.setContent(content);

		comment.setLevel(level);

		level++;
		comment.setChildren(parseList(element, level));

		return comment;
	}

	private DeletedComment parseDeletedComment(Element element, int level) {

		DeletedComment comment = new DeletedComment();

		Element divElement = element.select(".deleted").first();
		String idString = divElement.attr("id");
		String[] split = idString.split("-");
		idString = split[1];
		comment.setId(Long.parseLong(idString));

		String content = element.select(".content").first().html();
		comment.setContent(content);

		comment.setLevel(level);

		level++;
		comment.setChildren(parseList(element, level));

		return comment;
	}

}
