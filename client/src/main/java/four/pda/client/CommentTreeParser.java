package four.pda.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import four.pda.client.model.AbstractComment;
import four.pda.client.model.Comment;
import four.pda.client.model.DeletedComment;

/**
 * Created by asavinova on 02/11/15.
 */
public class CommentTreeParser extends AbstractParser {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy | HH:ss");

	public List<AbstractComment> parse(String pageSource) {

		Document document = Jsoup.parse(pageSource);
		Element element = document.select("div#comments").first();
		return findCommentList(element, 0);
	}

	private List<AbstractComment> findCommentList(Element rootElement, int level) {
		Elements children = rootElement.children();
		for (Element element : children) {
			if (element.hasClass("comment-list")) {
				return parseCommentList(element.children(), level);
			}
		}
		return null;
	}

	private List<AbstractComment> parseCommentList(Elements elements, int level) {
		List<AbstractComment> comments = new ArrayList<>();
		for (Element element : elements) {
			AbstractComment comment = parseComment(element, level);
			if (comment == null) {
				continue;
			}
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

		String content = element.select(".content").first().html();
		comment.setContent(content);

		comment.setLevel(level);

		level++;
		comment.setCommentList(findCommentList(element, level));

		return comment;
	}

	private DeletedComment parseDeletedComment(Element element, int level) {
		DeletedComment comment = new DeletedComment();

		Element divElement = element.select(".deleted").first();
		String idString = divElement.attr("id");
		String[] split = idString.split("-");
		if (split.length > 1) {
			if (split == null) {
				return null;
			}
			idString = split[1];
			comment.setId(Long.parseLong(idString));
		} else {
			//TODO
			return null;
		}

		String content = element.select(".content").first().html();
		comment.setContent(content);

		comment.setLevel(level);

		level++;
		comment.setCommentList(findCommentList(element, level));

		return comment;
	}

}
