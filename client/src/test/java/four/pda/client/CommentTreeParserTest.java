package four.pda.client;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import four.pda.client.model.Comment;

/**
 * Created by asavinova on 02/11/15.
 */
public class CommentTreeParserTest extends AbstractTest {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy HH:ss");

	@Test
	public void firstComment() throws IOException {
		String pageSource = getHtmlSource("/2014/10/27/182819/");
		List<Comment> comments = new CommentTreeParser().parse(pageSource);

		Assert.assertTrue(comments.size() > 0);

		Comment firstComment = comments.get(0);
		Assert.assertEquals("Wrong comment id", 1945077, firstComment.getId());
		Assert.assertEquals("Wrong comment nickname", "Qwertymisha", firstComment.getNickname());
		Assert.assertEquals("Wrong comment content", "Норм", firstComment.getContent());
		Assert.assertEquals("Wrong comment level", 0, firstComment.getLevel());
		Assert.assertEquals("Wrong comment date", "27.10.14 13:41", DATE_FORMAT.format(firstComment.getDate()));
	}

	@Test
	public void childComments() throws IOException {
		String pageSource = getHtmlSource("/2014/10/27/182819/");
		List<Comment> comments = new CommentTreeParser().parse(pageSource);

		Assert.assertTrue(comments.size() > 0);

		Comment secondComment = comments.get(1);
		List<Comment> childComments = secondComment.getCommentList();

		Comment child1 = childComments.get(0);
		Assert.assertEquals("Wrong comment id", 1945383, child1.getId());
		Assert.assertEquals("Wrong comment nickname", "keeapk", child1.getNickname());
		Assert.assertEquals("Wrong comment content", "xbrat, Как ей пользоваться чего то не понял ?:)", child1.getContent());
		Assert.assertEquals("Wrong comment level", 1, child1.getLevel());
		Assert.assertEquals("Wrong comment date", "27.10.14 15:46", DATE_FORMAT.format(child1.getDate()));
	}

}
