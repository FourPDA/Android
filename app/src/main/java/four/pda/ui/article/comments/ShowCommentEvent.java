package four.pda.ui.article.comments;

import four.pda.client.model.Comment;

/**
 * Created by asavinova on 25/04/16.
 */
public class ShowCommentEvent {

	private Comment comment;

	public ShowCommentEvent(Comment comment) {
		this.comment = comment;
	}

	public Comment getComment() {
		return comment;
	}

}
